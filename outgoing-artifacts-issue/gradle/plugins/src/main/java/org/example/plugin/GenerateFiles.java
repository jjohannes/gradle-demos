package org.example.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;

import java.io.IOException;
import java.nio.file.Files;

public abstract class GenerateFiles extends DefaultTask {

    @InputFiles
    public abstract Property<String> getContent();

    @OutputFiles
    public abstract ListProperty<RegularFile> getOutputFiles();

    @TaskAction
    public void generate() throws IOException {
        for (RegularFile file : getOutputFiles().get()) {
            Files.writeString(file.getAsFile().toPath(), getContent().get());
        }
    }
}
