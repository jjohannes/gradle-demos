package software.onepiece.toolchain.task;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Nested;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolRepositoryInfo;
import software.onepiece.toolchain.service.ToolInstallService;

import javax.inject.Inject;
import java.io.File;

public interface ToolUsingTask extends Task {
    @Nested
    ListProperty<ToolInfo> getTools();

    @Input
    ListProperty<ToolRepositoryInfo> getToolRepositories();

    @Inject
    ObjectFactory getObjects();

    @Inject
    Gradle getGradle();

    default void toolRepository(String url) {
        toolRepository(url, a -> {});
    }

    default void toolRepository(String url, Action<ToolRepositoryInfo> action) {
        ToolRepositoryInfo repo = getObjects().newInstance(ToolRepositoryInfo.class);
        repo.getUrl().set(url);
        action.execute(repo);
        getToolRepositories().add(repo);
    }

    default void tool(String group, String name, String version) {
        tool(group, name, version, a -> {});
    }

    default void tool(String group, String name, String version, Action<ToolInfo> action) {
        File homeDir = getGradle().getGradleUserHomeDir();
        ToolInfo tool = getObjects().newInstance(ToolInfo.class);
        tool.getGroup().set(group);
        tool.getName().set(name);
        tool.getVersion().set(version);
        tool.getInstallationDirectory().set(new File(homeDir, "tools/" + tool.id().replace(":", "_")));
        tool.getToolRegistryDirectory().set(new File(homeDir, "tools/.registry"));
        action.execute(tool);
        getTools().add(tool);
    }

    default void installTools() {
        getObjects().newInstance(ToolInstallService.class).installTools(getTools().get(), getToolRepositories().get());
    }
}
