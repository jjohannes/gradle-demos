package org.example;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;

public interface ToolInfo {
    ConfigurableFileCollection getArchive();
    DirectoryProperty getInstallationDirectory();
    DirectoryProperty getGradleUserHomeDir();
    Property<String> getExecutable();
}
