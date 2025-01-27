package software.onepiece.toolchain.extract;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.gradle.api.logging.Logger;
import org.gradle.internal.os.OperatingSystem;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ExtractUtil {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ExtractUtil.class);

    public static void extractArchive(File archive, File destination) {
        String archiveName = archive.getName().toLowerCase();
        long startTimeMillis = System.currentTimeMillis();
        LOGGER.lifecycle("Tool Transform Start Time : [" + archive.getAbsolutePath() + "] : [" + startTimeMillis + "]");

        try {
            if (archiveName.endsWith(".7z")) {
                extract7z(archive, destination);
            } else {
                extractZip(archive, destination);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long endTimeMillis = System.currentTimeMillis();
        long transformTimeMillis = (endTimeMillis - startTimeMillis);
        LOGGER.lifecycle("Tool Transform End Time : [" + archive.getAbsolutePath() + "] : [" + endTimeMillis + "]");
        LOGGER.lifecycle("Tool Transform Time : [" + archive.getAbsolutePath() + "] : [" + transformTimeMillis + "]");
    }

    private static void extractZip(File archive, File destination) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(Files.newInputStream(archive.toPath()));
        ZipEntry zipEntry = zis.getNextEntry();
        if (zipEntry != null) {
            while (zipEntry != null) {
                File extractedFile = new File(destination, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    extractedFile.mkdirs();
                } else {
                    extractedFile.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(extractedFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    if (OperatingSystem.current().isUnix()) {
                        Files.setPosixFilePermissions(extractedFile.toPath(), PosixFilePermissions.fromString("rwxr-xr-x"));
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } else {
            zis.closeEntry();
            zis.close();
            extract7z(archive, destination); // fallback to 7z
        }
    }

    private static void extract7z(File archive, File destination) throws IOException {
        SevenZFile sevenZFile = new SevenZFile.Builder().setFile(archive).get();
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();
        while (entry != null) {
            File extractedFile = new File(destination, entry.getName());
            if (entry.isDirectory()) {
                extractedFile.mkdirs();
            } else {
                extractedFile.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(extractedFile);
                byte[] content = new byte[(int) entry.getSize()];
                sevenZFile.read(content, 0, content.length);
                out.write(content);
                out.close();
            }
            entry = sevenZFile.getNextEntry();
        }
        sevenZFile.close();
    }
}
