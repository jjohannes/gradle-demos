package software.onepiece.toolchain;

import org.gradle.api.Action;
import org.gradle.api.file.BuildLayout;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;

import javax.inject.Inject;

public abstract class ToolsExtension {

    protected abstract ListProperty<ToolRepositoryInfo> getRepositories();

    protected abstract MapProperty<String, ToolInfo> getTools();

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract BuildLayout getLayout();

    @Inject
    protected abstract Gradle getGradle();

    public ToolsExtension() {
        getGradle().getSharedServices().registerIfAbsent("toolInstall", ToolInstallService.class, spec -> {
            spec.getParameters().getRepositories().set(getRepositories());
            spec.getParameters().getTools().set(getTools());
        });

        ToolsRegistryExtension toolsRegistry = getObjects().newInstance(ToolsRegistryExtension.class, getTools());
        getGradle().getLifecycle().beforeProject(p -> p.getExtensions().add("toolsRegistry", toolsRegistry));
    }

    public void repository(String url) {
        repository(url, a -> {});
    }

    public void repository(String url, Action<ToolRepositoryInfo> action) {
        ToolRepositoryInfo repo = getObjects().newInstance(ToolRepositoryInfo.class);
        repo.getUrl().set(url);
        action.execute(repo);
        getRepositories().add(repo);
    }

    public void register(String id, String group, String name, String version, String executable) {
        ToolInfo tool = getObjects().newInstance(ToolInfo.class);
        tool.getGroup().set(group);
        tool.getName().set(name);
        tool.getVersion().set(version);
        tool.getGradleUserHomeDir().set(getGradle().getGradleUserHomeDir());
        tool.getInstallationDirectory().set(
                getLayout().getSettingsDirectory().dir("tools-installations/" + name + "-" + version));
        tool.getExecutable().set(executable);
        getTools().put(id, tool);
    }

}
