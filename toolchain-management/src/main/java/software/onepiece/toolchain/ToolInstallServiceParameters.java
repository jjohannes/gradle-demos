package software.onepiece.toolchain;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.BuildServiceParameters;

public interface ToolInstallServiceParameters extends BuildServiceParameters {
    ListProperty<ToolRepositoryInfo> getRepositories();
    MapProperty<String, ToolInfo> getTools();
    Property<ToolInstallServicesProvider> getToolServices();
}
