package org.example.tasks;

import org.gradle.api.Task;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.Internal;

public interface TaskWithWorkDir extends Task {
    @Internal
    DirectoryProperty getWorkDirPath();
}
