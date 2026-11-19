package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.api.Action;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.operations.BuildOperationQueue;
import org.gradle.internal.process.ArgWriter;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.nativeplatform.internal.LinkerSpec;
import org.gradle.nativeplatform.toolchain.internal.AbstractCompiler;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocation;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocationWorker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Linker extends AbstractCompiler<LinkerSpec> {
    public Linker(BuildOperationExecutor buildOperationExecutor, CommandLineToolInvocationWorker commandLineToolInvocationWorker, CommandLineToolContext invocationContext, WorkerLeaseService workerLeaseService) {
        super(buildOperationExecutor, commandLineToolInvocationWorker, invocationContext,
                new LinkerArgsTransformer(),
                true, workerLeaseService);
    }

    @Override
    protected Action<BuildOperationQueue<CommandLineToolInvocation>> newInvocationAction(final LinkerSpec spec, List<String> args) {
        final CommandLineToolInvocation invocation = newInvocation("linking " + spec.getOutputFile().getName(), args, spec.getOperationLogger());

        return buildQueue -> {
            buildQueue.setLogLocation(spec.getOperationLogger().getLogLocation());
            buildQueue.add(invocation);
        };
    }

    @Override
    protected void addOptionsFileArgs(List<String> args, File tempDir) {
        ArrayList<String> originalArgs = new ArrayList<>(args);
        args.clear();
        args.addAll(ArgWriter.argsFileGenerator(new File(tempDir, "options.txt"), ArgWriter.unixStyleFactory()).transform(originalArgs));
    }
}
