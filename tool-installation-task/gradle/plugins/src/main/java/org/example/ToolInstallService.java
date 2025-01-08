package org.example;

import net.lingala.zip4j.ZipFile;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.logging.Logger;
import org.gradle.api.services.BuildService;
import org.gradle.internal.vfs.FileSystemAccess;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Collections.singleton;

public abstract class ToolInstallService implements BuildService<ToolInstallServiceParameters>{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ToolInstallService.class);

    @Inject
    protected abstract FileSystemOperations getFiles();

    public ToolInfo getTool(String id, FileSystemAccess fileSystemAccess) {
        try {
            ToolInfo tool = getParameters().getTools().getting(id).get();

            File hashFile = hashFile(tool);

            String previousSnapshot = hashFile.exists() ? Files.readString(hashFile.toPath()) : "";

            String currentSnapshot = readSnapshot(tool, fileSystemAccess);

            boolean installationInvalid = !currentSnapshot.equals(previousSnapshot);

            if (installationInvalid) {
                LOGGER.lifecycle("Extracting: " + tool.getArchive().getSingleFile().getName());
                doExtractFile(tool, fileSystemAccess);
            } else {
                LOGGER.lifecycle("UP-TO-DATE: " + tool.getArchive().getSingleFile().getName());
            }

            return tool;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doExtractFile(ToolInfo tool, FileSystemAccess fileSystemAccess) throws IOException {
        File installDir = tool.getInstallationDirectory().get().getAsFile();

        getFiles().delete(f -> f.delete(installDir));
        try (ZipFile zipFile = new ZipFile(tool.getArchive().getSingleFile())) {
            zipFile.extractAll(installDir.getAbsolutePath());
        }

        fileSystemAccess.invalidate(singleton(installDir.getAbsolutePath()));

        //noinspection ResultOfMethodCallIgnored
        hashFile(tool).getParentFile().mkdirs();
        Files.writeString(hashFile(tool).toPath(), readSnapshot(tool, fileSystemAccess));
    }

    private String readSnapshot(ToolInfo tool, FileSystemAccess fileSystemAccess) {
        File installDir = tool.getInstallationDirectory().get().getAsFile();
        return fileSystemAccess.read(installDir.getAbsolutePath()).getHash().toString();
    }

    private File hashFile(ToolInfo tool) {
        File archive = tool.getArchive().getSingleFile();
        return tool.getGradleUserHomeDir().get().dir("artifact-extraction/" + archive.getName() + ".hash").getAsFile();
    }
}
