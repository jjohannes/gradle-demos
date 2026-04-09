import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.FileSystemOperations;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;

@CacheableTask
public abstract class CodeGenTask extends DefaultTask {

    @Input
    public abstract Property<String> getMessage();

    @OutputDirectory
    public abstract DirectoryProperty getDestinationDir();

    @Inject
    protected abstract FileSystemOperations getFiles();

    @TaskAction
    public void generate() throws IOException {
        getFiles().delete(d -> d.delete(getDestinationDir()));
        var out = getDestinationDir().getAsFile().get().toPath().resolve("org/example");
        Files.createDirectories(out);
        Files.writeString(out.resolve("App.java"), """
                package org.example;
                
                public class App {
                    public static void main(String[] args) {
                        System.out.println("%s");
                    }
                }
                """.formatted(getMessage().get()));
    }
}
