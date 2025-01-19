package software.onepiece.toolchain;

import org.gradle.api.Task;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.gradle.api.tasks.Input;
import org.gradle.internal.vfs.FileSystemAccess;

import javax.inject.Inject;

public interface ToolUsingTask extends Task {
    @Input
    Property<String> getToolId();

    @ServiceReference("toolInstall")
    Property<ToolInstallService> getToolInstall();

    @Inject
    FileSystemAccess getFileSystemAccess();
}
