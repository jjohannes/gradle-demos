package org.example.tasks;

import org.example.work.CommandExecutorWorkAction;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkerExecutor;

import javax.inject.Inject;

abstract public class CommandExecutorTask extends DefaultTask implements TaskWithWorkDir {

    @Inject
    abstract public WorkerExecutor getWorkerExecutor();

    @Input
    abstract public ListProperty<String> getCommand();

    @TaskAction
    public void execute() {
        getWorkerExecutor().classLoaderIsolation().submit(CommandExecutorWorkAction.class, p -> {
            p.getTaskName().set(getName());
            p.getWorkDirPath().set(getWorkDirPath());
            p.getCommand().set(getCommand());
        });
    }
}
