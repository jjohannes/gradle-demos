package example.plugin;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

abstract public class GreetTask extends DefaultTask {

    @TaskAction
    public void greet() {
        System.out.println("Hello from Gradle 7");
    }
}
