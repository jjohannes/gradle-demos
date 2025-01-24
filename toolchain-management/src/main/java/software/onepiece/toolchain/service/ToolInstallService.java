package software.onepiece.toolchain.service;

import net.lingala.zip4j.ZipFile;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.internal.artifacts.DependencyResolutionServices;
import org.gradle.api.internal.initialization.StandaloneDomainObjectContext;
import org.gradle.api.logging.Logger;
import org.gradle.api.services.BuildService;
import org.gradle.internal.vfs.FileSystemAccess;
import org.slf4j.LoggerFactory;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolRepositoryInfo;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

public abstract class ToolInstallService implements BuildService<ToolInstallServiceParameters>{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ToolInstallService.class);

    @Inject
    protected abstract FileSystemOperations getFiles();

    public List<ToolInfo> getTools(List<String> ids) {
        return ids.stream().map((String id) -> getParameters().getTools().getting(id).get()).collect(Collectors.toList());
    }

    public List<ToolInfo> getTools(List<String> ids, ToolInstallServicesProvider services) {
        return ids.stream().map((String id) -> getTool(id, services)).collect(Collectors.toList());
    }

    private ToolInfo getTool(String id, ToolInstallServicesProvider services) {
        try {
            ToolInfo tool = getParameters().getTools().getting(id).get();
            boolean useFromFolder = tool.getFromFolder().isPresent();

            File from = useFromFolder
                    ? new File(tool.getFromFolder().get())
                    : createArchiveResolver(tool, services).getSingleFile();
            File hashFile = hashFile(tool, from);

            if (isInstallationInvalid(tool, hashFile, services)) {
                synchronized (tool) {
                    // still invalid after synchronize?
                    if (isInstallationInvalid(tool, hashFile, services)) {
                        if (useFromFolder) {
                            LOGGER.lifecycle("Copying: " + from.getName());
                            copyFolder(tool, from, services);
                        } else {
                            LOGGER.lifecycle("Extracting: " + from.getName());
                            extractFile(tool, from, services);
                        }
                    }
                }
            } else {
                LOGGER.lifecycle("UP-TO-DATE: " + from.getName());
            }

            return tool;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInstallationInvalid(ToolInfo tool, File hashFile, ToolInstallServicesProvider services) throws IOException {
        String previousSnapshot = hashFile.exists() ? Files.readString(hashFile.toPath()) : "";
        String currentSnapshot = readSnapshot(tool, services);
        return !currentSnapshot.equals(previousSnapshot);
    }

    private FileCollection createArchiveResolver(ToolInfo tool, ToolInstallServicesProvider services) {
        DependencyResolutionServices dmServices = services.getDependencyManagementServices().newDetachedResolver(
                services.getDependencyFileResolver(),
                services.getFileCollectionFactory(),
                StandaloneDomainObjectContext.ANONYMOUS
        );
        RepositoryHandler repositories = dmServices.getResolveRepositoryHandler();
        for (ToolRepositoryInfo repo : getParameters().getRepositories().get()) {
            if (repo.getPattern().isPresent()) {
                // Custom Ivy
                repositories.ivy(ivy ->
                {
                    ivy.setUrl(repo.getUrl().get());
                    ivy.patternLayout(p ->
                    {
                        p.artifact(repo.getPattern().get());
                        p.setM2compatible(true);
                    });
                    ivy.metadataSources(IvyArtifactRepository.MetadataSources::artifact);
                    if (repo.getUsername().isPresent()) {
                        ivy.getCredentials().setUsername(repo.getUsername().get());
                        ivy.getCredentials().setPassword(repo.getPassword().get());
                    }
                });
            } else {
                // Standard Maven
                repositories.maven(maven -> {
                    maven.setUrl(repo.getUrl().get());
                    if (repo.getUsername().isPresent()) {
                        maven.getCredentials().setUsername(repo.getUsername().get());
                        maven.getCredentials().setPassword(repo.getPassword().get());
                    }
                });
            }
        }
        Configuration tools = dmServices.getConfigurationContainer().dependencyScope("tools").get();
        Configuration resolver = dmServices.getConfigurationContainer().resolvable("toolDownload").get();
        resolver.extendsFrom(tools);

        String gav = tool.getGroup().get() + ":" + tool.getName().get() + ":" + tool.getVersion().get();
        dmServices.getDependencyHandler().add(tools.getName(), gav);

        return resolver;
    }

    private void copyFolder(ToolInfo tool, File from, ToolInstallServicesProvider services) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        getFiles().delete(f -> f.delete(installDir));
        getFiles().copy(spec -> {
           spec.from(from);
           spec.into(installDir);
        });
        snapshot(tool, from, services);
    }

    private void extractFile(ToolInfo tool, File from, ToolInstallServicesProvider services) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        getFiles().delete(f -> f.delete(installDir));
        try (ZipFile zipFile = new ZipFile(from)) {
            zipFile.extractAll(installDir.getAbsolutePath());
        }
        snapshot(tool, from, services);
    }

    private void snapshot(ToolInfo tool, File from, ToolInstallServicesProvider services) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        FileSystemAccess fileSystemAccess = services.getFileSystemAccess();
        fileSystemAccess.invalidate(singleton(installDir.getAbsolutePath()));

        Files.createDirectories(hashFile(tool, from).getParentFile().toPath());
        Files.writeString(hashFile(tool, from).toPath(), readSnapshot(tool, services));
    }

    private String readSnapshot(ToolInfo tool, ToolInstallServicesProvider services) {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        FileSystemAccess fileSystemAccess = services.getFileSystemAccess();
        if (tool.getExcludes().get().isEmpty()) {
            return fileSystemAccess.read(installDir.getAbsolutePath()).getHash().toString();
        } else {
            return fileSystemAccess.read(installDir.getAbsolutePath(),
                    new ToolExcludesFilter(tool.getExcludes().get())
            ).orElseThrow(IllegalStateException::new).getHash().toString();
        }
    }

    private File hashFile(ToolInfo tool, File archive) {
        return tool.getToolRegistryDirectory().get().dir(archive.getName() + ".hash").getAsFile();
    }
}
