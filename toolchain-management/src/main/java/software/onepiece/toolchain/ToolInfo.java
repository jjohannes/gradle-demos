package software.onepiece.toolchain;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.Optional;

public interface ToolInfo {
    @Input
    Property<String> getGroup();
    @Input
    Property<String> getName();
    @Input
    Property<String> getVersion();

    @Input
    ListProperty<String> getExcludes(); // Exclude folders/files when checking installation folder
    @Input
    @Optional
    Property<String> getFromFolder(); // If set, copy tool from the folder instead of downloading

    @Input
    Property<String> getExecutable();

    @Internal
    DirectoryProperty getInstallationDirectory();
    @Internal
    DirectoryProperty getToolRegistryDirectory();

    default String id() {
        return (getGroup().get() + ":" + getName().get() + ":" + getVersion().get()).intern();
    }
}
