package org.example.plainc.toolchain.tools;

import org.gradle.internal.Transformers;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.process.ArgWriter;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.nativeplatform.internal.CompilerOutputFileNamingSchemeFactory;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocationWorker;
import org.gradle.nativeplatform.toolchain.internal.NativeCompiler;
import org.gradle.nativeplatform.toolchain.internal.compilespec.CCompileSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CCompiler extends NativeCompiler<CCompileSpec> {

    public CCompiler(BuildOperationExecutor buildOperationExecutor, CompilerOutputFileNamingSchemeFactory compilerOutputFileNamingSchemeFactory, CommandLineToolInvocationWorker commandLineToolInvocationWorker, CommandLineToolContext invocationContext, String objectFileExtension, WorkerLeaseService workerLeaseService) {
        super(buildOperationExecutor, compilerOutputFileNamingSchemeFactory, commandLineToolInvocationWorker, invocationContext,
                new CCompilerArgsTransformer(), Transformers.noOpTransformer(),
                objectFileExtension, true, workerLeaseService);
    }

    @Override
    protected List<String> getOutputArgs(CCompileSpec spec, File outputFile) {
        return Arrays.asList("-o", outputFile.getAbsolutePath());
    }

    @Override
    protected void addOptionsFileArgs(List<String> args, File tempDir) {
        ArrayList<String> originalArgs = new ArrayList<>(args);
        args.clear();
        args.addAll(ArgWriter.argsFileGenerator(new File(tempDir, "options.txt"), ArgWriter.unixStyleFactory()).transform(originalArgs));
    }

    @Override
    protected List<String> getPCHArgs(CCompileSpec spec) {
        List<String> pchArgs = new ArrayList<>();
        if (spec.getPrefixHeaderFile() != null) {
            pchArgs.add("-include");
            pchArgs.add(spec.getPrefixHeaderFile().getAbsolutePath());
        }
        return pchArgs;
    }
}
