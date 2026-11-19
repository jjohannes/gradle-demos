package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.tasks.WorkResult;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.operations.BuildOperationQueue;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.nativeplatform.internal.StaticLibraryArchiverSpec;
import org.gradle.nativeplatform.toolchain.internal.AbstractCompiler;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocation;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocationWorker;

import java.io.File;
import java.util.List;

public class StaticLibraryArchiver extends AbstractCompiler<StaticLibraryArchiverSpec> {

    public StaticLibraryArchiver(BuildOperationExecutor buildOperationExecutor, CommandLineToolInvocationWorker commandLineToolInvocationWorker, CommandLineToolContext invocationContext, WorkerLeaseService workerLeaseService) {
        super(buildOperationExecutor, commandLineToolInvocationWorker, invocationContext, new StaticLibraryArchiverArgsTransformer(), false, workerLeaseService);
    }

    @Override
    public WorkResult execute(final StaticLibraryArchiverSpec spec) {
        deletePreviousOutput(spec);
        return super.execute(spec);
    }

    private void deletePreviousOutput(StaticLibraryArchiverSpec spec) {
        // Need to delete the previous archive, otherwise stale object files will remain
        if (!spec.getOutputFile().isFile()) {
            return;
        }
        if (!(spec.getOutputFile().delete())) {
            throw new GradleException("Create static archive failed: could not delete previous archive");
        }
    }

    @Override
    protected Action<BuildOperationQueue<CommandLineToolInvocation>> newInvocationAction(final StaticLibraryArchiverSpec spec, List<String> args) {
        final CommandLineToolInvocation invocation = newInvocation(
                "archiving " + spec.getOutputFile().getName(), spec.getOutputFile().getParentFile(), args, spec.getOperationLogger());

        return buildQueue -> {
            buildQueue.setLogLocation(spec.getOperationLogger().getLogLocation());
            buildQueue.add(invocation);
        };
    }

    @Override
    protected void addOptionsFileArgs(List<String> args, File tempDir) {
        // No support for command file
    }
}
