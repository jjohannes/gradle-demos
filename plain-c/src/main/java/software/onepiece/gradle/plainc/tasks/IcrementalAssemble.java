package software.onepiece.gradle.plainc.tasks;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.WorkResult;
import org.gradle.internal.operations.logging.BuildOperationLogger;
import org.gradle.language.assembler.internal.DefaultAssembleSpec;
import org.gradle.language.assembler.tasks.Assemble;
import org.gradle.language.base.internal.compile.Compiler;
import org.gradle.language.base.internal.tasks.StaleOutputCleaner;
import org.gradle.nativeplatform.internal.BuildOperationLoggingCompilerDecorator;
import org.gradle.nativeplatform.platform.internal.NativePlatformInternal;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainInternal;
import org.gradle.nativeplatform.toolchain.internal.compilespec.AssembleSpec;
import org.gradle.work.ChangeType;
import org.gradle.work.FileChange;
import org.gradle.work.Incremental;
import org.gradle.work.InputChanges;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Incremental and cacheable version of the 'org.gradle.language.assembler.tasks.Assemble' task.
 */
@CacheableTask
public abstract class IcrementalAssemble extends Assemble {

    @Incremental
    @Override
    public ConfigurableFileCollection getSource() {
        return super.getSource();
    }

    @TaskAction
    public void incrementalAssemble(InputChanges inputs) {
        // Copied from super class and adjusted for incremental assembling

        BuildOperationLogger operationLogger = getOperationLoggerFactory().newOperationLogger(getName(), getTemporaryDir());

        List<File> toDelete = new ArrayList<>();
        List<File> toAssemble = new ArrayList<>();

        for (FileChange change : inputs.getFileChanges(super.getSource())) {
            if (change.getChangeType() == ChangeType.REMOVED) {
                toDelete.add(change.getFile());
            }
            if (change.getChangeType() != ChangeType.REMOVED) {
                toAssemble.add(change.getFile());
            }
        }

        boolean cleanedOutputs = StaleOutputCleaner.cleanOutputs(
                getDeleter(),
                toDelete,
                getObjectFileDir()
        );

        DefaultAssembleSpec spec = new DefaultAssembleSpec();
        spec.setTempDir(getTemporaryDir());

        spec.setObjectFileDir(getObjectFileDir());
        spec.source(toAssemble);
        spec.include(getIncludes());
        spec.args(getAssemblerArgs());
        spec.setOperationLogger(operationLogger);

        NativeToolChainInternal nativeToolChain = (NativeToolChainInternal) getToolChain().get();
        NativePlatformInternal nativePlatform = (NativePlatformInternal) getTargetPlatform().get();
        Compiler<AssembleSpec> compiler = nativeToolChain.select(nativePlatform).newCompiler(AssembleSpec.class);
        WorkResult result = BuildOperationLoggingCompilerDecorator.wrap(compiler).execute(spec);
        setDidWork(result.getDidWork() || cleanedOutputs);
    }

    @Override
    public void assemble() {
        // disable assemble() without 'inputs' argument
    }
}
