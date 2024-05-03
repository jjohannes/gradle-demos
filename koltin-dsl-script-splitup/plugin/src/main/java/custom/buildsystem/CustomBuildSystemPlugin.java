package custom.buildsystem;

import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

public class CustomBuildSystemPlugin implements Plugin<Settings> {

    @Override
    public void apply(Settings settings) {
        settings.getGradle().beforeProject(p -> p.getPlugins().apply(MainPlugin.class));
    }
}
