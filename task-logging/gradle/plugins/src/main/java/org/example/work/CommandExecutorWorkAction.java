package org.example.work;

import org.gradle.process.ExecOperations;
import org.gradle.workers.WorkAction;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.example.work.LogUtil.logFileForWorkDir;

abstract public class CommandExecutorWorkAction implements WorkAction<CommandExecutorWorkParameters> {

    private final Logger log = Logger.getLogger(getParameters().getTaskName().get());
    private final File logFile = logFileForWorkDir(getParameters().getWorkDirPath(), getParameters().getTaskName().get());

    public CommandExecutorWorkAction() throws IOException {
        if (logFile.exists()) {
            logFile.delete(); // delete existing file (?)
        }
        if (log.getHandlers().length > 0) {
            log.removeHandler(log.getHandlers()[0]);
        }
        logFile.getParentFile().mkdirs();
        logFile.createNewFile();

        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                try {
                    Files.write(logFile.toPath(), (record.getMessage() + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void flush() {  }

            @Override
            public void close() throws SecurityException {}
        };
        log.addHandler(handler);
    }

    @Inject
    abstract public ExecOperations getExecOperations();

    @Override
    public void execute() {
        log.warning("An early log from Java");

        getExecOperations().exec(exe -> {
            exe.commandLine(getParameters().getCommand().get());
            try {
                exe.setStandardOutput(new FileOutputStream(logFile, true));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        log.warning("A late log from Java");
    }
}
