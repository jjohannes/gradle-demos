package org.example;

import org.gradle.api.flow.FlowAction;
import org.gradle.api.flow.FlowParameters;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;

@SuppressWarnings("UnstableApiUsage")
public class BuildResultFlowAction implements FlowAction<BuildResultFlowAction.Parameters> {

    static String buildResultMessage = "";

    public interface Parameters extends FlowParameters {
        @Input
        Property<String> getBuildResultMessage();
    }

    @Override
    public void execute(Parameters parameters) {
        buildResultMessage = parameters.getBuildResultMessage().get();
    }
}
