package software.onepiece.toolchain;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

@NonNullApi
public abstract class ToolchainManagementPlugin implements Plugin<Settings> {

    @Override
    public void apply(Settings settings) {
        settings.getExtensions().create("tools", ToolsExtension.class);
    }
}
