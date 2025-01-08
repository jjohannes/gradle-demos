package org.example.tasks;

import org.example.ToolInfo;
import org.example.ToolInstallService;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.Input;
import org.gradle.internal.vfs.FileSystemAccess;

import javax.inject.Inject;

public abstract class ToolBasedExec extends Exec {

    @ServiceReference("toolInstall")
    protected abstract Property<ToolInstallService> getToolInstall();

    @Input
    public abstract Property<String> getToolId();

    @Inject
    protected abstract FileSystemAccess getFileSystemAccess();

    @Override
    protected void exec() {
        ToolInfo tool = getToolInstall().get().getTool(getToolId().get(), getFileSystemAccess());
        setExecutable(tool.getInstallationDirectory().file(tool.getExecutable()).get().getAsFile());
        super.exec();
    }
}
