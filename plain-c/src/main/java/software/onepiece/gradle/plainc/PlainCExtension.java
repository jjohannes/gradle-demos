package software.onepiece.gradle.plainc;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.file.FileCollection;
import org.gradle.api.model.ObjectFactory;
import org.gradle.nativeplatform.platform.NativePlatform;
import org.gradle.nativeplatform.platform.internal.DefaultArchitecture;
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform;
import org.gradle.nativeplatform.platform.internal.DefaultOperatingSystem;
import org.gradle.nativeplatform.toolchain.NativeToolChain;
import software.onepiece.gradle.plainc.toolchain.PlainCNativeToolChain;

import javax.inject.Inject;
import java.io.File;

public abstract class PlainCExtension {

    static final Attribute<Boolean> EXTRACTED_TOOLS_ATTRIBUTE = Attribute.of("extracted-tools", Boolean.class);

    static final NativePlatform GENERIC_NATIVE_PLATFORM = new DefaultNativePlatform(
            "generic_platform",
            new DefaultOperatingSystem("generic_os"),
            new DefaultArchitecture("generic_arch")
    );

    public NativeToolChain localTool(String version, String location, String objectFileExtension) {
        File tool = new File(location);
        return getObjects().newInstance(PlainCNativeToolChain.class, version, tool.getName(), getObjects().fileCollection().from(tool), objectFileExtension);
    }

    public NativeToolChain repositoryTool(String group, String name, String version, String locationInZip, String objectFileExtension) {
        Configuration toolConfiguration = getConfigurations().detachedConfiguration(
                getDependencies().create(group + ":" + name + ":" + version + "@zip"));
        FileCollection tool = toolConfiguration.getIncoming().artifactView(a -> a.getAttributes().attribute(EXTRACTED_TOOLS_ATTRIBUTE, true))
                .getFiles().getAsFileTree().filter(file -> file.getPath().endsWith(locationInZip));

        return getObjects().newInstance(PlainCNativeToolChain.class, name, version, tool, objectFileExtension);
    }

    public NativePlatform platform() {
        return GENERIC_NATIVE_PLATFORM;
    }

    @Inject
    abstract protected ObjectFactory getObjects();

    @Inject
    abstract protected ConfigurationContainer getConfigurations();

    @Inject
    abstract protected DependencyHandler getDependencies();
}
