package software.onepiece.toolchain.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkerExecutor;
import software.onepiece.toolchain.worker.PrintToolWorkAction;

import javax.inject.Inject;

@CacheableTask
abstract public class PrintToolInfo extends DefaultTask implements ToolUsingTask {

    @OutputFile
    abstract public RegularFileProperty getResult();

    @Inject
    abstract protected WorkerExecutor getExecutor();

    @TaskAction
    protected void execute() {
        getExecutor().noIsolation().submit(PrintToolWorkAction.class, a -> {
            a.getToolInstall().set(getToolInstall());
            a.getToolIds().set(getToolIds());
            a.getResult().set(getResult());
        });
    }
}
