package custom.buildsystem;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MainPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getExtensions().create("__mydsl", MyDslExtension.class);
    }
}
