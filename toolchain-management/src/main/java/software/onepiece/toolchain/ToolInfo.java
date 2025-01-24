package software.onepiece.toolchain;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public interface ToolInfo {
    Property<String> getName();
    Property<String> getGroup();
    Property<String> getVersion();

    ListProperty<String> getExcludes(); // Exclude folders/files when checking installation folder
    Property<String> getFromFolder(); // If set, copy tool from the folder instead of downloading

    Property<String> getExecutable();

    DirectoryProperty getInstallationDirectory();
    DirectoryProperty getToolRegistryDirectory();
}
