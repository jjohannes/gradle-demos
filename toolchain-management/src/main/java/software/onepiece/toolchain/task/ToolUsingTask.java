package software.onepiece.toolchain.task;

import org.gradle.api.Task;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.gradle.api.tasks.Input;
import software.onepiece.toolchain.service.ToolInstallService;

public interface ToolUsingTask extends Task {
    @Input
    ListProperty<String> getToolIds();

    @ServiceReference("toolInstall")
    Property<ToolInstallService> getToolInstall();
}
