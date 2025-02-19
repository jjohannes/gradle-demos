package software.onepiece.toolchain;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@NonNullApi
abstract public class ToolInstallPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) { }
}
