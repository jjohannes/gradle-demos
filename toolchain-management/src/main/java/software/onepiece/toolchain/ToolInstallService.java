package software.onepiece.toolchain;

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
        return ids.stream().map(this::getTool).collect(Collectors.toList());
    }

    private ToolInfo getTool(String id) {
        try {
            ToolInfo tool = getParameters().getTools().getting(id).get();
            File toolArchive = createArchiveResolver(tool).getSingleFile();
            File hashFile = hashFile(tool, toolArchive);
            String previousSnapshot = hashFile.exists() ? Files.readString(hashFile.toPath()) : "";
            String currentSnapshot = readSnapshot(tool);

            boolean installationInvalid = !currentSnapshot.equals(previousSnapshot);

            if (installationInvalid) {
                LOGGER.lifecycle("Extracting: " + toolArchive.getName());
                doExtractFile(tool, toolArchive);
            } else {
                LOGGER.lifecycle("UP-TO-DATE: " + toolArchive.getName());
            }

            return tool;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileCollection createArchiveResolver(ToolInfo tool) {
        ToolInstallServicesProvider services = getParameters().getToolServices().get();
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

    private void doExtractFile(ToolInfo tool, File toolArchive) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();

        getFiles().delete(f -> f.delete(installDir));
        try (ZipFile zipFile = new ZipFile(toolArchive)) {
            zipFile.extractAll(installDir.getAbsolutePath());
        }

        FileSystemAccess fileSystemAccess = getParameters().getToolServices().get().getFileSystemAccess();
        fileSystemAccess.invalidate(singleton(installDir.getAbsolutePath()));

        Files.createDirectories(hashFile(tool, toolArchive).getParentFile().toPath());
        Files.writeString(hashFile(tool, toolArchive).toPath(), readSnapshot(tool));
    }

    private String readSnapshot(ToolInfo tool) {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        FileSystemAccess fileSystemAccess = getParameters().getToolServices().get().getFileSystemAccess();
        return fileSystemAccess.read(installDir.getAbsolutePath()).getHash().toString();
    }

    private File hashFile(ToolInfo tool, File archive) {
        return tool.getGradleUserHomeDir().get().dir("artifact-extraction/" + archive.getName() + ".hash").getAsFile();
    }
}
