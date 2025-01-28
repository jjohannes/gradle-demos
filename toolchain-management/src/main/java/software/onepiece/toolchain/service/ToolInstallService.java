package software.onepiece.toolchain.service;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.internal.artifacts.DependencyManagementServices;
import org.gradle.api.internal.artifacts.DependencyResolutionServices;
import org.gradle.api.internal.file.FileCollectionFactory;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.initialization.StandaloneDomainObjectContext;
import org.gradle.api.logging.Logger;
import org.gradle.internal.vfs.FileSystemAccess;
import org.slf4j.LoggerFactory;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolRepositoryInfo;
import software.onepiece.toolchain.extract.ExtractUtil;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.util.Collections.singleton;

public abstract class ToolInstallService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ToolInstallService.class);

    @Inject
    protected abstract FileSystemAccess getFileSystemAccess();
    @Inject
    protected abstract DependencyManagementServices getDependencyManagementServices();
    @Inject
    protected abstract FileResolver getDependencyFileResolver();
    @Inject
    protected abstract FileCollectionFactory getFileCollectionFactory();

    @Inject
    protected abstract FileSystemOperations getFiles();

    public void installTools(List<ToolInfo> ids, List<ToolRepositoryInfo> toolRepositories) {
        ids.forEach(tool -> installTool(tool, toolRepositories));
    }

    private void installTool(ToolInfo tool, List<ToolRepositoryInfo> toolRepositories) {
        try {
            boolean useFromFolder = tool.getFromFolder().isPresent();

            File hashFile = hashFile(tool);

            if (isInstallationInvalid(tool, hashFile)) {
                synchronized (tool.id()) {
                    // still invalid after synchronize?
                    if (isInstallationInvalid(tool, hashFile)) {
                        File from = useFromFolder
                                ? new File(tool.getFromFolder().get())
                                : createArchiveResolver(tool, toolRepositories).getSingleFile();

                        if (useFromFolder) {
                            LOGGER.lifecycle("Copying: " + from.getName());
                            copyFolder(tool, from);
                        } else {
                            LOGGER.lifecycle("Extracting: " + from.getName());
                            extractFile(tool, from);
                        }
                    }
                }
            } else {
                LOGGER.lifecycle("UP-TO-DATE: " + tool.id());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isInstallationInvalid(ToolInfo tool, File hashFile) throws IOException {
        String previousSnapshot = hashFile.exists() ? Files.readString(hashFile.toPath()) : "";
        String currentSnapshot = readSnapshot(tool);
        return !currentSnapshot.equals(previousSnapshot);
    }

    private FileCollection createArchiveResolver(ToolInfo tool, List<ToolRepositoryInfo> toolRepositories) {
        DependencyResolutionServices dmServices = getDependencyManagementServices().newDetachedResolver(
                getDependencyFileResolver(),
                getFileCollectionFactory(),
                StandaloneDomainObjectContext.ANONYMOUS
        );
        RepositoryHandler repositories = dmServices.getResolveRepositoryHandler();
        for (ToolRepositoryInfo repo : toolRepositories) {
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

        dmServices.getDependencyHandler().add(tools.getName(), tool.id());

        return resolver;
    }

    private void copyFolder(ToolInfo tool, File from) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        getFiles().delete(f -> f.delete(installDir));
        getFiles().copy(spec -> {
            spec.from(from);
            spec.into(installDir);
        });
        snapshot(tool);
    }

    private void extractFile(ToolInfo tool, File from) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        getFiles().delete(f -> f.delete(installDir));
        ExtractUtil.extractArchive(from, installDir);
        snapshot(tool);
    }

    private void snapshot(ToolInfo tool) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        FileSystemAccess fileSystemAccess = getFileSystemAccess();
        fileSystemAccess.invalidate(singleton(installDir.getAbsolutePath()));

        Files.createDirectories(hashFile(tool).getParentFile().toPath());
        Files.writeString(hashFile(tool).toPath(), readSnapshot(tool));
    }

    private String readSnapshot(ToolInfo tool) {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        FileSystemAccess fileSystemAccess = getFileSystemAccess();
        if (tool.getExcludes().get().isEmpty()) {
            return fileSystemAccess.read(installDir.getAbsolutePath()).getHash().toString();
        } else {
            return fileSystemAccess.read(installDir.getAbsolutePath(),
                    new ToolExcludesFilter(tool.getExcludes().get())
            ).orElseThrow(IllegalStateException::new).getHash().toString();
        }
    }

    private File hashFile(ToolInfo tool) {
        return tool.getToolRegistryDirectory().get().dir(tool.id().replace(":", "_") + ".hash").getAsFile();
    }
}
