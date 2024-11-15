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

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@NonNullApi
public abstract class ToFileLoggingListener implements BuildOperationListener {

    @Inject
    protected abstract BuildOperationListenerManager getBuildOperationListenerManager();

    private final FileWriter logFileWriter;
    private final PrintWriter logPrintWriter;

    @Inject
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public ToFileLoggingListener(File logFile) throws IOException {
        logFile.getParentFile().mkdirs();
        logFileWriter = new FileWriter(logFile);
        logPrintWriter = new PrintWriter(logFileWriter);
    }

    @Override
    public void started(BuildOperationDescriptor buildOperation, OperationStartEvent startEvent) {
    }

    @Override
    public void progress(OperationIdentifier operationIdentifier, OperationProgressEvent progressEvent) {
        Object details = progressEvent.getDetails();
        String plainLogOutput = null;

        if (details instanceof StyledTextOutputEvent) {
            StyledTextOutputEvent styledLogOutput = (StyledTextOutputEvent) details;
            plainLogOutput = styledLogOutput.getSpans().stream().map(StyledTextOutputEvent.Span::getText).collect(Collectors.joining());
        } else if (details instanceof LogEvent) {
            LogEvent logEvent = (LogEvent) details;
            plainLogOutput = logEvent.getMessage() + "\n";
        }

        printToLog(plainLogOutput, null);
    }

    @Override
    public void finished(BuildOperationDescriptor buildOperation, OperationFinishEvent finishEvent) {
        printToLog(null, finishEvent.getFailure());
        if (finishEvent.getResult() instanceof FinishRootBuildTreeBuildOperationType.Result) {
            getBuildOperationListenerManager().removeListener(this);
            try {
                logPrintWriter.close();
                logFileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void printToLog(@Nullable String plainLogOutput, @Nullable Throwable exception) {
        try {
            if (plainLogOutput != null) {
                logFileWriter.write(plainLogOutput);
            }
            if (exception != null) {
                exception.printStackTrace(logPrintWriter);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}