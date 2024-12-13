package org.example;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;

import javax.inject.Inject;

@NonNullApi
public abstract class ToolInstallationPlugin implements Plugin<Project> {

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public void apply(Project project) {
        project.getExtensions().create("tools", ToolsExtension.class);
    }
}