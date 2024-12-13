package org.example;

import net.lingala.zip4j.ZipFile;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.vfs.FileSystemAccess;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.Collections.singleton;

public abstract class ToolInstall extends DefaultTask {

    @InputFiles
    @PathSensitive(PathSensitivity.NAME_ONLY)
    public abstract ConfigurableFileCollection getArchive();

    @OutputDirectory
    public abstract DirectoryProperty getInstallationDirectory();

    @Internal
    protected abstract DirectoryProperty getGradleUserHomeDir();

    @Inject
    protected abstract FileSystemOperations getFiles();

    @Inject
    protected abstract FileSystemAccess getFileSystemAccess();

    @TaskAction
    public void maybeExtract() throws IOException {
        File hashFile = hashFile();
        String previousSnapshot = hashFile.exists() ? Files.readString(hashFile.toPath()) : "";
        String currentSnapshot = readSnapshot();

        boolean installationInvalid = !currentSnapshot.equals(previousSnapshot);

        if (installationInvalid) {
            getLogger().lifecycle("Extracting: " + getArchive().getSingleFile().getName());
            doExtractFile();
        } else {
            getLogger().lifecycle("UP-TO-DATE: " + getArchive().getSingleFile().getName());
        }
    }

    private void doExtractFile() throws IOException {
        File installDir = getInstallationDirectory().get().getAsFile();

        getFiles().delete(f -> f.delete(installDir));
        try (ZipFile zipFile = new ZipFile(getArchive().getSingleFile())) {
            zipFile.extractAll(installDir.getAbsolutePath());
        }

        getFileSystemAccess().invalidate(singleton(installDir.getAbsolutePath()));

        //noinspection ResultOfMethodCallIgnored
        hashFile().getParentFile().mkdirs();
        Files.writeString(hashFile().toPath(), readSnapshot());
    }

    private String readSnapshot() {
        File installDir = getInstallationDirectory().get().getAsFile();
        return getFileSystemAccess().read(installDir.getAbsolutePath()).getHash().toString();
    }

    private File hashFile() {
        File archive = getArchive().getSingleFile();
        return getGradleUserHomeDir().get().dir("artifact-extraction/" + archive.getName() + ".hash").getAsFile();
    }
}
