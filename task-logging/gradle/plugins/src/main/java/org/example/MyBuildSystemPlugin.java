package org.example;

import org.example.plainc.PlainCExtension;
import org.example.tasks.CommandExecutorTask;
import org.example.tasks.ExtendedCCompile;
import org.example.tasks.TaskWithWorkDir;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;

import java.util.List;

@SuppressWarnings("unused")
abstract public class MyBuildSystemPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        ProjectLayout layout = project.getLayout();
        PlainCExtension plainc = project.getExtensions().create("plainc", PlainCExtension.class);

        project.getTasks().withType(TaskWithWorkDir.class).configureEach(t -> {
            t.getWorkDirPath().set(layout.getBuildDirectory().dir("workspaces/" + t.getName()));
        });

        project.getTasks().register("execCat", CommandExecutorTask.class, t -> {
            t.setGroup("system");
            t.setDescription("Run CAT");
            t.getCommand().set(List.of("cat", "README.md"));
        });

        project.getTasks().register("execLs", CommandExecutorTask.class, t -> {
            t.setGroup("system");
            t.setDescription("Run LS");
            t.getCommand().set(List.of("ls", "-al"));
        });

        project.getTasks().register("cCompile", ExtendedCCompile.class, t -> {
            t.setGroup("system");
            t.setDescription("Compile C");

            t.source(layout.getProjectDirectory().files("src/c").getAsFileTree());
            t.includes(layout.getProjectDirectory().files("src/headers"));
            t.getCompilerArgs().add("-S");

            t.getToolChain().set(plainc.localTool("14.0.0", "/usr/bin/clang", ".o"));
            t.getTargetPlatform().set(plainc.platform());

            t.getObjectFileDir().set(layout.getBuildDirectory().dir("workspaces/" + t.getName()));
        });
    }
}
