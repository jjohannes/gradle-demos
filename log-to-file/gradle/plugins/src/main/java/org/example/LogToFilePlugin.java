package org.example;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.initialization.Settings;
import org.gradle.api.model.ObjectFactory;
import org.gradle.internal.operations.BuildOperationListenerManager;

import javax.inject.Inject;
import java.io.File;

@NonNullApi
public abstract class LogToFilePlugin implements Plugin<Settings> {

    @Inject
    protected abstract BuildOperationListenerManager getBuildOperationListenerManager();

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public void apply(Settings settings) {
        File logFile = new File(settings.getRootDir(), "build/log-" + System.currentTimeMillis() + ".txt");
        getBuildOperationListenerManager().addListener(getObjects().newInstance(ToFileLoggingListener.class, logFile));
    }
}
