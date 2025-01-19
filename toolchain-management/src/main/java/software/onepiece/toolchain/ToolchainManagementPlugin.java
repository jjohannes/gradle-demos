package software.onepiece.toolchain;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@NonNullApi
public abstract class ToolchainManagementPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().create("tools", ToolsExtension.class);
    }
}
