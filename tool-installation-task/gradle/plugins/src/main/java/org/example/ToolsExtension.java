package org.example;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.file.Directory;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.internal.vfs.FileSystemAccess;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ToolsExtension {

    protected abstract MapProperty<String, ToolInfo> getTools();

    @Inject
    protected abstract ObjectFactory getObjects();

    @Inject
    protected abstract ProjectLayout getLayout();

    @Inject
    protected abstract DependencyHandler getDependencies();

    @Inject
    protected abstract ConfigurationContainer getConfigurations();

    @Inject
    protected abstract Gradle getGradle();

    public ToolsExtension() {
        getGradle().getSharedServices().registerIfAbsent("toolInstall", ToolInstallService.class, spec -> {
            spec.getParameters().getTools().set(getTools());
        });
    }

    public void register(String id, String group, String name, String version, String executable) {
        Configuration resolver = getConfigurations().detachedConfiguration(getDependencies().create(
                group + ":" + name + ":" + version));

        ToolInfo tool = getObjects().newInstance(ToolInfo.class);
        tool.getArchive().from(resolver);
        tool.getGradleUserHomeDir().set(getGradle().getGradleUserHomeDir());
        tool.getInstallationDirectory().set(
                getLayout().getProjectDirectory().dir("tools-installations/" + name + "-" + version));
        tool.getExecutable().set(executable);
        getTools().put(id, tool);
    }
}
