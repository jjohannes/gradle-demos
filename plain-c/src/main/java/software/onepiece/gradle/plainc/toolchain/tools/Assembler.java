package software.onepiece.gradle.plainc.toolchain.tools;

import org.gradle.internal.Transformers;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.process.ArgWriter;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.nativeplatform.internal.CompilerOutputFileNamingSchemeFactory;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocation;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocationWorker;
import org.gradle.nativeplatform.toolchain.internal.NativeCompiler;
import org.gradle.nativeplatform.toolchain.internal.compilespec.AssembleSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assembler extends NativeCompiler<AssembleSpec> {

    private final String objectFileExtension;

    public Assembler(BuildOperationExecutor buildOperationExecutor, CompilerOutputFileNamingSchemeFactory compilerOutputFileNamingSchemeFactory, CommandLineToolInvocationWorker commandLineTool, CommandLineToolContext invocationContext, String objectFileExtension, WorkerLeaseService workerLeaseService) {
        super(buildOperationExecutor, compilerOutputFileNamingSchemeFactory, commandLineTool, invocationContext,
                new AssemblerArgsTransformer(), Transformers.noOpTransformer(),
                objectFileExtension, true,
                workerLeaseService);

        this.objectFileExtension = objectFileExtension;
    }

    @Override
    protected CommandLineToolInvocation createPerFileInvocation(List<String> genericArgs, File sourceFile, File objectDir, AssembleSpec spec) {
        List<String> sourceArgs = this.getSourceArgs(sourceFile);
        List<String> outputArgs = this.getOutputArgs(spec, this.getOutputFileDir(sourceFile, objectDir, objectFileExtension));
        List<String> pchArgs = this.maybeGetPCHArgs(spec, sourceFile);

        // Changed order: put 'outputArgs' first
        return this.newInvocation("compiling ".concat(sourceFile.getName()), objectDir, this.buildPerFileArgs(outputArgs, genericArgs, sourceArgs, pchArgs), spec.getOperationLogger());
    }

    @Override
    protected Iterable<String> buildPerFileArgs(List<String> genericArgs, List<String> sourceArgs, List<String> outputArgs, List<String> pchArgs) {
        if (pchArgs != null && !pchArgs.isEmpty()) {
            throw new UnsupportedOperationException("Precompiled header arguments cannot be specified for an Assembler compiler.");
        }
        return super.buildPerFileArgs(genericArgs, sourceArgs, outputArgs, pchArgs);
    }

    @Override
    protected List<String> getOutputArgs(AssembleSpec spec, File outputFile) {
        return Arrays.asList("-o", outputFile.getAbsolutePath());
    }

    @Override
    protected void addOptionsFileArgs(List<String> args, File tempDir) {
        ArrayList<String> originalArgs = new ArrayList<>(args);
        args.clear();
        args.addAll(ArgWriter.argsFileGenerator(new File(tempDir, "options.txt"), ArgWriter.unixStyleFactory()).transform(originalArgs));
    }

    @Override
    protected List<String> getPCHArgs(AssembleSpec spec) {
        List<String> pchArgs = new ArrayList<>();
        if (spec.getPrefixHeaderFile() != null) {
            pchArgs.add("-include");
            pchArgs.add(spec.getPrefixHeaderFile().getAbsolutePath());
        }
        return pchArgs;
    }
}