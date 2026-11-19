package software.onepiece.toolchain.worker;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.ToolRepositoryInfo;
import software.onepiece.toolchain.service.ToolInstallService;

import javax.inject.Inject;

public abstract class ToolUsingWorkAction<T extends ToolUsingWorkAction.Params> implements WorkAction<T> {
    public interface Params extends WorkParameters {
        ListProperty<ToolInfo> getTools();
        ListProperty<ToolRepositoryInfo> getToolRepositories();
    }

    static public abstract class Default extends ToolUsingWorkAction<Params> { }

    @Inject
    protected abstract ObjectFactory getObjects();

    @Override
    public void execute() {
        getObjects().newInstance(ToolInstallService.class).installTools(
                getParameters().getTools().get(), getParameters().getToolRepositories().get());
    }
}
