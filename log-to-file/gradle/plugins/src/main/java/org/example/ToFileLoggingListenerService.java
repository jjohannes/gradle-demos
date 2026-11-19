package org.example;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.services.BuildService;
import org.gradle.api.services.BuildServiceParameters;
import org.gradle.tooling.events.FinishEvent;
import org.gradle.tooling.events.OperationCompletionListener;
import org.gradle.tooling.events.task.TaskFinishEvent;

import javax.inject.Inject;

/**
 * Service to register a {@link ToFileLoggingListener} even if the build configuration is restored from cache.
 */
public abstract class ToFileLoggingListenerService implements BuildService<ToFileLoggingListenerService.Parameters>, OperationCompletionListener
{

    public interface Parameters extends BuildServiceParameters
    {
        RegularFileProperty getRootDir();
    }

    @Inject
    protected abstract ObjectFactory getObjects();

    private final ToFileLoggingListener toFileLoggingListener;

    public ToFileLoggingListenerService()
    {
        toFileLoggingListener = getObjects().newInstance(ToFileLoggingListener.class,
                getParameters().getRootDir().get().getAsFile());
    }

    @Override
    public void onFinish(FinishEvent finishEvent) {
        if (finishEvent instanceof TaskFinishEvent) {
            toFileLoggingListener.logTaskFinished((TaskFinishEvent) finishEvent);
        }
    }
}
