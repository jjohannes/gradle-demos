import com.gradle.enterprise.gradleplugin.GradleEnterpriseExtension;
import com.gradle.enterprise.gradleplugin.GradleEnterprisePlugin;
import com.gradle.scan.plugin.BuildScanExtension;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;

import java.io.File;

public class MySmartSettingPlugin implements Plugin<Settings> {
    @Override
    public void apply(Settings settings) {

        settings.getPlugins().apply(GradleEnterprisePlugin.class);

        File projectPlugins = new File(settings.getRootDir().getParentFile(), "project-plugins");
        if (projectPlugins.exists()) {
            System.out.println("Java Settings Plugin - pluginManagement.includeBuild: " + projectPlugins);
            settings.getPluginManagement().includeBuild(projectPlugins.getPath());
        }

        BuildScanExtension buildScan = settings.getExtensions().getByType(GradleEnterpriseExtension.class).getBuildScan();
        buildScan.publishAlways();
        buildScan.setTermsOfServiceUrl("https://gradle.com/terms-of-service");
        buildScan.setTermsOfServiceAgree("yes");
    }
}
