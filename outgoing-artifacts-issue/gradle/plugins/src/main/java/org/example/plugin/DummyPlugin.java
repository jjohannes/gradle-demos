package org.example.plugin;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@NonNullApi
public abstract class DummyPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) { }
}
