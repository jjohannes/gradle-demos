package org.example;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.Directory;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ToolsExtension {

    // If we use a property instead of the field the task dependency carried
    // by Provider<Directory> is lost for some reason.
    //   protected abstract MapProperty<String, Directory> getRegistry();
    private final Map<String, Provider<Directory>> registry = new LinkedHashMap<>();

    @Inject
    protected abstract ProjectLayout getLayout();

    @Inject
    protected abstract TaskContainer getTasks();

    @Inject
    protected abstract DependencyHandler getDependencies();

    @Inject
    protected abstract ConfigurationContainer getConfigurations();

    @Inject
    protected abstract Gradle getGradle();

    public void register(String id, String group, String name, String version) {
        Configuration resolver = getConfigurations().detachedConfiguration(getDependencies().create(
                group + ":" + name + ":" + version));
        String taskName = "install_" + name + "_" + version;
        TaskProvider<ToolInstall> task = getTasks().register(taskName, ToolInstall.class, t -> {
            t.setGroup("install");
            t.getArchive().from(resolver);
            t.getGradleUserHomeDir().set(getGradle().getGradleUserHomeDir());
            t.getInstallationDirectory().set(
                    getLayout().getProjectDirectory().dir("tools-installations/" + name + "-" + version));
        });
        registry.put(id, task.flatMap(ToolInstall::getInstallationDirectory));
    }

    public Provider<Directory> byId(String id) {
        return registry.get(id);
    }
}
