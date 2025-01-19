package software.onepiece.toolchain;

import org.gradle.api.provider.Property;

public interface ToolRepositoryInfo {
    Property<String> getUrl();
    Property<String> getPattern();
    Property<String> getUsername();
    Property<String> getPassword();

}
