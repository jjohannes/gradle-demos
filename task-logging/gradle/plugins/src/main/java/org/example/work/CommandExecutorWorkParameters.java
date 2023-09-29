package org.example.work;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;

public interface CommandExecutorWorkParameters extends WorkParameters {
    Property<String> getTaskName();
    DirectoryProperty getWorkDirPath();
    ListProperty<String> getCommand();
}
