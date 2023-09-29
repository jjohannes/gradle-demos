package org.example.tasks;

import org.gradle.api.tasks.CacheableTask;
import org.gradle.language.c.tasks.CCompile;
import org.gradle.nativeplatform.toolchain.internal.NativeCompileSpec;
import org.gradle.work.InputChanges;

import java.io.File;

import static org.example.work.LogUtil.logFileForWorkDir;

@CacheableTask
public abstract class ExtendedCCompile extends CCompile implements TaskWithWorkDir {

    @Override
    protected void configureSpec(NativeCompileSpec spec) {
        super.configureSpec(spec);

        File logFile = logFileForWorkDir(getWorkDirPath(), getName());
        // set folder to which the "output.txt" log file is written
        spec.setOperationLogger(getOperationLoggerFactory().newOperationLogger(getName(), logFile.getParentFile()));
    }


    protected void compile(InputChanges inputs) {
        super.compile(inputs);
        // rename "output.txt" to something else as last step of task action
        File logFile = logFileForWorkDir(getWorkDirPath(), getName());
        new File(logFile, "output.txt").renameTo(logFile);
    }
}
