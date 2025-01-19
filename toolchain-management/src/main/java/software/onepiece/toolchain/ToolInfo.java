package software.onepiece.toolchain;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

public interface ToolInfo {
    Property<String> getName();
    Property<String> getGroup();
    Property<String> getVersion();

    Property<String> getExecutable();
    DirectoryProperty getInstallationDirectory();
    DirectoryProperty getGradleUserHomeDir();
}
