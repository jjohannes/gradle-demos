package software.onepiece.toolchain.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolUsingTask;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

abstract public class PrintToolInfo extends DefaultTask implements ToolUsingTask {
    @OutputFile
    abstract public RegularFileProperty getResult();

    @TaskAction
    protected void execute() throws IOException {
        List<ToolInfo> tools = getToolInstall().get().getTools(getToolIds().get(), toolServices());

        Files.writeString(getResult().get().getAsFile().toPath(),
                "Used: " + tools.stream().map(t -> t.getExecutable().get()).collect(Collectors.joining()));
    }
}
