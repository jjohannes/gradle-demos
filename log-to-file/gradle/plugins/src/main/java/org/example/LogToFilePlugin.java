package org.example;

import org.gradle.api.Plugin;
import org.gradle.api.flow.FlowProviders;
import org.gradle.api.flow.FlowScope;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Provider;
import org.gradle.api.services.BuildServiceRegistry;
import org.gradle.build.event.BuildEventsListenerRegistry;

import javax.inject.Inject;
import java.io.File;

@SuppressWarnings("UnstableApiUsage")
public abstract class LogToFilePlugin implements Plugin<Settings> {

    @Inject
    protected abstract FlowScope getFlowScope();

    @Inject
    protected abstract FlowProviders getFlowProviders();

    @Inject
    protected abstract BuildEventsListenerRegistry getEventsListenerRegistry();

    @Override
    public void apply(Settings settings) {
        configureLogging(settings);
    }

    private void configureLogging(Settings settings) {
        BuildServiceRegistry services = settings.getGradle().getSharedServices();
        Provider<ToFileLoggingListenerService> buildOperationListenerService =
                services.registerIfAbsent("buildOperationListenerService", ToFileLoggingListenerService.class,
                        s -> s.getParameters().getRootDir().set(new File(settings.getRootDir(), "build")));
        // Use this to make Gradle reconstruct the service, even when running with Configuration Cache
        getEventsListenerRegistry().onTaskCompletion(buildOperationListenerService);

        // Use a 'flow action' as it seems to be the only way to get the build result in a way compatible with the
        // Configuration Cache.
        getFlowScope().always(BuildResultFlowAction.class, spec -> {
            spec.getParameters().getBuildResultMessage()
                    .set(getFlowProviders().getBuildWorkResult()
                            .map(result -> result.getFailure().isPresent() ? "BUILD FAILED" : "BUILD SUCCESSFUL"));
        });
    }
}
