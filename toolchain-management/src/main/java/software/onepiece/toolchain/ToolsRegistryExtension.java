package software.onepiece.toolchain;

import org.gradle.api.provider.Provider;

import javax.inject.Inject;
import java.util.Map;

public abstract class ToolsRegistryExtension {

    private final Provider<Map<String, ToolInfo>> registry;

    @Inject
    public ToolsRegistryExtension(Provider<Map<String, ToolInfo>> registry) {
        this.registry = registry;
    }

    public Map<String, ToolInfo> getRegistry() {
        return registry.get();
    }
}
