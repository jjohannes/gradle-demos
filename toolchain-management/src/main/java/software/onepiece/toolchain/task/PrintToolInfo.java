package software.onepiece.toolchain.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

@CacheableTask
abstract public class PrintToolInfo extends DefaultTask implements ToolUsingTask {

    @OutputFile
    abstract public RegularFileProperty getResult();

    @Inject
    abstract protected WorkerExecutor getExecutor();

    @TaskAction
    protected void execute() {
        installTools();
        try {
            Files.writeString(getResult().get().getAsFile().toPath(),
                    "Used: " + getTools().get().stream().map(t -> t.getExecutable().get()).collect(Collectors.joining()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
