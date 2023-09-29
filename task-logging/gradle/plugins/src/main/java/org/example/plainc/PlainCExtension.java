package org.example.plainc;

import org.example.plainc.toolchain.PlainCNativeToolChain;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.platform.NativePlatform;
import org.gradle.nativeplatform.platform.internal.DefaultArchitecture;
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform;
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem;
import org.gradle.nativeplatform.toolchain.NativeToolChain;

import javax.inject.Inject;
import java.io.File;

// simplified version of: https://github.com/jjohannes/plain-c
public abstract class PlainCExtension {

    static final NativePlatform GENERIC_NATIVE_PLATFORM = new DefaultNativePlatform(
            "generic_platform",
            new DefaultOperatingSystem("generic_os"),
            new DefaultArchitecture("generic_arch")
    );

    public NativeToolChain localTool(String version, String location, String objectFileExtension) {
        File tool = new File(location);
        return getObjects().newInstance(PlainCNativeToolChain.class, version, tool.getName(),
                getObjects().fileCollection().from(tool), objectFileExtension);
    }

    public NativePlatform platform() {
        return GENERIC_NATIVE_PLATFORM;
    }

    @Inject
    abstract protected ObjectFactory getObjects();
}
