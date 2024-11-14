package org.example;

import org.gradle.api.NonNullApi;
import org.gradle.internal.logging.events.LogEvent;
import org.gradle.internal.logging.events.StyledTextOutputEvent;
import org.gradle.internal.operations.BuildOperationDescriptor;
import org.gradle.internal.operations.BuildOperationListener;
import org.gradle.internal.operations.BuildOperationListenerManager;
import org.gradle.internal.operations.OperationFinishEvent;
import org.gradle.internal.operations.OperationIdentifier;
import org.gradle.internal.operations.OperationProgressEvent;
import org.gradle.internal.operations.OperationStartEvent;
import org.gradle.operations.lifecycle.FinishRootBuildTreeBuildOperationType;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@NonNullApi
public abstract class ToFileLoggingListener implements BuildOperationListener {

    @Inject
    protected abstract BuildOperationListenerManager getBuildOperationListenerManager();

    private final File logFile;

    @Inject
    public ToFileLoggingListener(File logFile) {
        this.logFile = logFile;
    }

    @Override
    public void started(BuildOperationDescriptor buildOperation, OperationStartEvent startEvent) {
    }

    @Override
    public void progress(OperationIdentifier operationIdentifier, OperationProgressEvent progressEvent) {
        Object details = progressEvent.getDetails();
        if (details instanceof StyledTextOutputEvent) {
            StyledTextOutputEvent styledLogOutput = (StyledTextOutputEvent) details;
            String plainLogOutput = styledLogOutput.getSpans().stream().map(StyledTextOutputEvent.Span::getText).collect(Collectors.joining());
            try {
                Files.writeString(logFile.toPath(), plainLogOutput, CREATE, APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (details instanceof LogEvent) {
            LogEvent logEvent = (LogEvent) details;
            String plainLogOutput = logEvent.getMessage() + "\n";
             try {
                Files.writeString(logFile.toPath(), plainLogOutput, CREATE, APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void finished(BuildOperationDescriptor buildOperation, OperationFinishEvent finishEvent) {
        if (finishEvent.getResult() instanceof FinishRootBuildTreeBuildOperationType.Result) {
            getBuildOperationListenerManager().removeListener(this);
        }
    }
}