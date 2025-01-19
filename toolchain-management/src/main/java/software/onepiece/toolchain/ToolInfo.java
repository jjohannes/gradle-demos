package software.onepiece.toolchain;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

public interface ToolInfo {
    Property<String> getCoordinates();
    Property<String> getExecutable();
    DirectoryProperty getInstallationDirectory();
    DirectoryProperty getGradleUserHomeDir();
}
