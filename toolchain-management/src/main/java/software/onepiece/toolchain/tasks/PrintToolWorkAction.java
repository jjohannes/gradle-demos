package software.onepiece.toolchain.tasks;

import org.gradle.api.file.RegularFileProperty;
import software.onepiece.toolchain.ToolUsingWorkAction;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

abstract public class PrintToolWorkAction extends ToolUsingWorkAction<PrintToolWorkAction.Param> {
    interface Param extends ToolUsingWorkAction.Param {
        RegularFileProperty getResult();
    }

    public void execute() {
        super.execute();
        try {
            Files.writeString(getParameters().getResult().get().getAsFile().toPath(),
                    "Used: " + tools.stream().map(t -> t.getExecutable().get()).collect(Collectors.joining()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
