package software.onepiece.toolchain.service;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.services.BuildServiceParameters;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolRepositoryInfo;

public interface ToolInstallServiceParameters extends BuildServiceParameters {
    ListProperty<ToolRepositoryInfo> getRepositories();
    MapProperty<String, ToolInfo> getTools();
}
