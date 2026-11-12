package software.onepiece.gradle.plainc.toolchain;

import org.gradle.api.NonNullApi;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.platform.internal.NativePlatformInternal;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import org.gradle.nativeplatform.toolchain.internal.NativeLanguage;
import org.gradle.nativeplatform.toolchain.internal.NativeToolChainInternal;
import org.gradle.nativeplatform.toolchain.internal.PlatformToolProvider;

import javax.inject.Inject;

@NonNullApi
public abstract class PlainCNativeToolChain implements NativeToolChain, NativeToolChainInternal {

    private final String toolName;
    private final String toolVersion;
    private final FileCollection tool;
    private final String objectFileExtension;

    @Inject
    public PlainCNativeToolChain(String toolName, String toolVersion, FileCollection tool, String objectFileExtension) {
        this.toolName = toolName;
        this.toolVersion = toolVersion;
        this.tool = tool;
        this.objectFileExtension = objectFileExtension;
    }

    @Override
    public String getName() {
        return toolName + "_" + toolVersion;
    }

    @Override
    public PlatformToolProvider select(NativePlatformInternal nativePlatform) {
        return getObjects().newInstance(PlainCPlatformToolProvider.class, tool, toolVersion, objectFileExtension);
    }

    @Override
    public PlatformToolProvider select(NativeLanguage nativeLanguage, NativePlatformInternal nativePlatform) {
        return select(nativePlatform);
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public String getOutputType() {
        return getName();
    }

    @Override
    public void assertSupported() { }

    @Inject
    abstract protected ObjectFactory getObjects();
}
