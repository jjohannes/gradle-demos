package software.onepiece.gradle.plainc.toolchain;

import org.gradle.api.file.FileCollection;
import org.gradle.internal.logging.text.DiagnosticsVisitor;
import org.gradle.internal.operations.BuildOperationExecutor;
import org.gradle.internal.work.WorkerLeaseService;
import org.gradle.language.base.internal.compile.CompileSpec;
import org.gradle.language.base.internal.compile.Compiler;
import org.gradle.language.base.internal.compile.DefaultCompilerVersion;
import org.gradle.language.base.internal.compile.VersionAwareCompiler;
import org.gradle.nativeplatform.internal.CompilerOutputFileNamingSchemeFactory;
import org.gradle.nativeplatform.internal.LinkerSpec;
import org.gradle.nativeplatform.internal.StaticLibraryArchiverSpec;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.CommandLineToolInvocationWorker;
import org.gradle.nativeplatform.toolchain.internal.DefaultCommandLineToolInvocationWorker;
import org.gradle.nativeplatform.toolchain.internal.DefaultMutableCommandLineToolContext;
import org.gradle.nativeplatform.toolchain.internal.EmptySystemLibraries;
import org.gradle.nativeplatform.toolchain.internal.OutputCleaningCompiler;
import org.gradle.nativeplatform.toolchain.internal.PlatformToolProvider;
import org.gradle.nativeplatform.toolchain.internal.SystemLibraries;
import org.gradle.nativeplatform.toolchain.internal.ToolType;
import org.gradle.nativeplatform.toolchain.internal.compilespec.AssembleSpec;
import org.gradle.nativeplatform.toolchain.internal.compilespec.CCompileSpec;
import org.gradle.nativeplatform.toolchain.internal.metadata.CompilerMetadata;
import org.gradle.nativeplatform.toolchain.internal.tools.CommandLineToolSearchResult;
import org.gradle.process.internal.ExecActionFactory;
import org.gradle.util.internal.VersionNumber;

import software.onepiece.gradle.plainc.toolchain.tools.Assembler;
import software.onepiece.gradle.plainc.toolchain.tools.CCompiler;
import software.onepiece.gradle.plainc.toolchain.tools.Linker;
import software.onepiece.gradle.plainc.toolchain.tools.StaticLibraryArchiver;

import javax.inject.Inject;

public abstract class PlainCPlatformToolProvider implements PlatformToolProvider {

    private final FileCollection tool;
    private final String toolVersion;
    private final String objectFileExtension;

    @Inject
    public PlainCPlatformToolProvider(FileCollection tool, String toolVersion, String objectFileExtension) {
        this.tool = tool;
        this.toolVersion = toolVersion;
        this.objectFileExtension = objectFileExtension;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CompileSpec> Compiler<T> newCompiler(Class<T> spec) {
        if (CCompileSpec.class.isAssignableFrom(spec)) {
            return (Compiler<T>) createCCompiler();
        }
        if (AssembleSpec.class.isAssignableFrom(spec)) {
            return (Compiler<T>) createAssembler();
        }
        if (LinkerSpec.class.isAssignableFrom(spec)) {
            return (Compiler<T>) createLinker();
        }
        if (StaticLibraryArchiverSpec.class.isAssignableFrom(spec)) {
            return (Compiler<T>) createStaticLibraryArchiver();
        }
        throw new IllegalArgumentException(String.format("Don't know how to compile from a spec of type %s.", spec.getSimpleName()));
    }

    protected Compiler<CCompileSpec> createCCompiler() {
        CCompiler cCompiler = new CCompiler(getBuildOperationExecutor(), getCompilerOutputFileNamingSchemeFactory(), commandLineTool("Compiler"), context(),
                objectFileExtension, getWorkerLeaseService());
        OutputCleaningCompiler<CCompileSpec> outputCleaningCompiler = new OutputCleaningCompiler<>(cCompiler, getCompilerOutputFileNamingSchemeFactory(), objectFileExtension);
        return versionAwareCompiler(outputCleaningCompiler, "C Compiler");
    }

    protected Compiler<AssembleSpec> createAssembler() {
        return new Assembler(getBuildOperationExecutor(), getCompilerOutputFileNamingSchemeFactory(), commandLineTool("Assembler"), context(), objectFileExtension, getWorkerLeaseService());
    }

    protected Compiler<LinkerSpec> createLinker() {
        Linker linker = new Linker(getBuildOperationExecutor(), commandLineTool("Linked"), context(), getWorkerLeaseService());
        return versionAwareCompiler(linker, "Linker");
    }

    protected Compiler<StaticLibraryArchiverSpec> createStaticLibraryArchiver() {
        return new StaticLibraryArchiver(getBuildOperationExecutor(), commandLineTool("Static Library"), context(), getWorkerLeaseService());
    }

    private <T extends CompileSpec> VersionAwareCompiler<T> versionAwareCompiler(Compiler<T> compiler, String toolType) {
        return new VersionAwareCompiler<>(compiler, new DefaultCompilerVersion(toolType, tool.getSingleFile().getName(), VersionNumber.parse(toolVersion)));
    }

    private CommandLineToolInvocationWorker commandLineTool(String name) {
        return new DefaultCommandLineToolInvocationWorker(name, tool.getSingleFile(), getExecActionFactory());
    }

    private CommandLineToolContext context() {
        return new DefaultMutableCommandLineToolContext();
    }

    @Override
    public String getExecutableName(String executablePath) {
        return executablePath;
    }

    @Override
    public String getSharedLibraryName(String libraryPath) {
        return libraryPath;
    }

    @Override
    public String getImportLibraryName(String libraryPath) {
        return libraryPath;
    }

    @Override
    public String getSharedLibraryLinkFileName(String libraryPath) {
        return libraryPath;
    }

    @Override
    public String getStaticLibraryName(String libraryPath) {
        return libraryPath;
    }

    @Override
    public String getExecutableSymbolFileName(String executablePath) {
        return executablePath;
    }

    @Override
    public String getLibrarySymbolFileName(String libraryPath) {
        return libraryPath;
    }

    @Override
    public SystemLibraries getSystemLibraries(ToolType compilerType) {
        return new EmptySystemLibraries();
    }


    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public boolean producesImportLibrary() {
        return false;
    }

    @Override
    public boolean requiresDebugBinaryStripping() {
        return true;
    }

    @Override
    public void explain(DiagnosticsVisitor visitor) {
    }

    @Override
    public String getObjectFileExtension() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T get(Class<T> toolType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompilerMetadata getCompilerMetadata(ToolType compilerType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CommandLineToolSearchResult locateTool(ToolType compilerType) {
        throw new UnsupportedOperationException();
    }

    @Inject
    protected abstract BuildOperationExecutor getBuildOperationExecutor();

    @Inject
    protected abstract CompilerOutputFileNamingSchemeFactory getCompilerOutputFileNamingSchemeFactory();

    @Inject
    protected abstract ExecActionFactory getExecActionFactory();

    @Inject
    protected abstract WorkerLeaseService getWorkerLeaseService();
}
