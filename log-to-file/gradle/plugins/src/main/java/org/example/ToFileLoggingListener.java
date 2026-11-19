package org.example;

import org.gradle.internal.buildevents.BuildStartedTime;
import org.gradle.internal.logging.events.LogEvent;
import org.gradle.internal.logging.events.StyledTextOutputEvent;
import org.gradle.internal.operations.BuildOperationDescriptor;
import org.gradle.internal.operations.BuildOperationListener;
import org.gradle.internal.operations.BuildOperationListenerManager;
import org.gradle.internal.operations.OperationFinishEvent;
import org.gradle.internal.operations.OperationIdentifier;
import org.gradle.internal.operations.OperationProgressEvent;
import org.gradle.internal.operations.OperationStartEvent;
import org.gradle.internal.time.TimeFormatting;
import org.gradle.operations.lifecycle.FinishRootBuildTreeBuildOperationType;
import org.gradle.tooling.events.OperationResult;
import org.gradle.tooling.events.task.TaskFinishEvent;
import org.gradle.tooling.events.task.TaskSuccessResult;
import org.jspecify.annotations.Nullable;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class ToFileLoggingListener implements BuildOperationListener
{
    @Inject
    protected abstract BuildOperationListenerManager getBuildOperationListenerManager();

    @Inject
    public abstract BuildStartedTime getBuildStartedTime();

    private final FileWriter logFileWriter;

    private final AtomicInteger upToDate = new AtomicInteger(0);
    private final AtomicInteger fromCache = new AtomicInteger(0);
    private final AtomicInteger executed = new AtomicInteger(0);

    @Inject
    public ToFileLoggingListener(File buildFolder) throws IOException {
        File logFile = new File(buildFolder, "log-" + System.currentTimeMillis() + ".txt");
        //noinspection ResultOfMethodCallIgnored
        logFile.getParentFile().mkdirs();
        this.logFileWriter = new FileWriter(logFile);
        getBuildOperationListenerManager().addListener(this);
    }

    void logTaskFinished(TaskFinishEvent event) {
        String taskMessage = event.getDisplayName()
                .replace(" SUCCESS", "")
                .replace(" skipped", " NO-SOURCE");

        printToLog("> " + taskMessage + "  \n");

        OperationResult result = event.getResult();
        if (result instanceof TaskSuccessResult) {
            if (((TaskSuccessResult) result).isUpToDate()) {
                upToDate.incrementAndGet();
            } else if (((TaskSuccessResult) result).isFromCache()) {
                fromCache.incrementAndGet();
            } else {
                executed.incrementAndGet();
            }
        } else { // TaskFailureResult
            executed.incrementAndGet();
        }
    }

    @Override
    public void started(BuildOperationDescriptor buildOperation, OperationStartEvent operationStartEvent) {
    }

    @Override
    public void progress(OperationIdentifier operationIdentifier, OperationProgressEvent progressEvent)
    {
        Object details = progressEvent.getDetails();
        String plainLogOutput = null;

        if (details instanceof StyledTextOutputEvent styledLogOutput)
        {
            String completeLog = styledLogOutput.getSpans().stream().map(StyledTextOutputEvent.Span::getText).collect(Collectors.joining());
            plainLogOutput = completeLog.replaceAll("\u001B\\[[0-9][0-9]?m", ""); // remove formating control characters like 'ESC[30m'
        } else if (details instanceof LogEvent logEvent)
        {
            plainLogOutput = logEvent.getMessage() + "\n";
        }

        printToLog(plainLogOutput);
    }

    @Override
    public void finished(BuildOperationDescriptor buildOperation, OperationFinishEvent finishEvent)
    {
        printToLog(finishEvent.getFailure());

        Object result = finishEvent.getResult();
        if (result instanceof FinishRootBuildTreeBuildOperationType.Result)
        {
            getBuildOperationListenerManager().removeListener(this);

            printResult();
            printSummary();

            try {
                logFileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inspired by {@link org.gradle.internal.buildevents.BuildResultLogger}.
     */
    public void printResult()
    {
        StringBuilder textOutput = new StringBuilder();
        textOutput.append("\n");
        textOutput.append(BuildResultFlowAction.buildResultMessage);
        long buildDurationMillis = System.currentTimeMillis() - getBuildStartedTime().getStartTime();
        textOutput.append(String.format(" in %s", TimeFormatting.formatDurationTerse(buildDurationMillis)));
        textOutput.append("\n");

        printToLog(textOutput.toString());
    }

    /**
     * Inspired by {@link org.gradle.internal.buildevents.TaskExecutionStatisticsReporter}.
     */
    private void printSummary()
    {
        int total = upToDate.get() + fromCache.get() + executed.get();
        if (total > 0)
        {
            String pluralizedTasks = total > 1 ? "tasks" : "task";
            StringBuilder textOutput = new StringBuilder();
            textOutput.append(String.format("%d actionable %s:", total, pluralizedTasks));
            boolean printedDetail = formatDetail(textOutput, executed.get(), "executed", false);
            printedDetail = formatDetail(textOutput, fromCache.get(), "from cache", printedDetail);
            formatDetail(textOutput, upToDate.get(), "up-to-date", printedDetail);

            printToLog(textOutput.toString());
        }
    }

    private boolean formatDetail(StringBuilder textOutput, int count, String title, boolean alreadyPrintedDetail)
    {
        if (count == 0)
        {
            return alreadyPrintedDetail;
        }
        if (alreadyPrintedDetail)
        {
            textOutput.append(",");
        }
        textOutput.append(String.format(" %d %s", count, title));
        return true;
    }

    private void printToLog(@Nullable String plainLogOutput)
    {
        try
        {
            if (plainLogOutput != null)
            {
                logFileWriter.write(plainLogOutput);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void printToLog(@Nullable Throwable exception)
    {
        if (exception != null)
        {
            printToLog(exception.getMessage());
        }
    }
}
