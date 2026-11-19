import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSetContainer;

public abstract class CodeGenPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().apply(JavaPlugin.class);

        var sourceSets = project.getExtensions().getByType(SourceSetContainer.class);

        var genCode = project.getTasks().register("genCode", CodeGenTask.class);

        sourceSets.named("main", main -> main.getJava().srcDir(genCode));
    }
}

