package org.example.work;

import org.gradle.api.file.DirectoryProperty;

import java.io.File;

public class LogUtil {

    public static File logFileForWorkDir(DirectoryProperty workDir, String taskName) {
        return workDir.file("logs/" + taskName + ".log").get().getAsFile();
    }
}
