package software.onepiece.toolchain.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolUsingTask;

import java.io.IOException;
import java.nio.file.Files;

abstract public class PrintToolInfo extends DefaultTask implements ToolUsingTask {
    @OutputFile
    abstract public RegularFileProperty getResult();

    @TaskAction
    protected void execute() throws IOException {
        ToolInfo tool = getToolInstall().get().getTool(getToolId().get());

        Files.writeString(getResult().get().getAsFile().toPath(),
                "Did something with " + tool.getExecutable().get());
    }
}
