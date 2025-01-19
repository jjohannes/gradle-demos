package software.onepiece.toolchain;

import org.gradle.api.provider.MapProperty;
import org.gradle.api.services.BuildServiceParameters;

public interface ToolInstallServiceParameters extends BuildServiceParameters {
    MapProperty<String, ToolInfo> getTools();
}
