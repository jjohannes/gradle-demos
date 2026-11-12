package software.onepiece.gradle.plainc.toolchain.transform;

import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.internal.os.OperatingSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ExtractZipTransform implements TransformAction<TransformParameters.None> {

    @InputArtifact
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract Provider<FileSystemLocation> getInputArtifact();

    @Override
    public void transform(TransformOutputs outputs) {
        File input = getInputArtifact().get().getAsFile();
        File unzipDir = outputs.dir(input.getName().substring(0, input.getName().lastIndexOf(".zip")));
        try {
            unzipTo(input, unzipDir);
            System.out.println("Transformed into: " + unzipDir.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unzipTo(File input, File unzipDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(input.toPath()));
        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                File newFile = new File(unzipDir, zipEntry.getName());
                //noinspection ResultOfMethodCallIgnored
                newFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                if (OperatingSystem.current().isUnix()) {
                    Files.setPosixFilePermissions(newFile.toPath(), PosixFilePermissions.fromString("rwxr-xr-x"));
                }
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }
}