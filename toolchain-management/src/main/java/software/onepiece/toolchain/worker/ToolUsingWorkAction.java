package software.onepiece.toolchain.worker;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;
import software.onepiece.toolchain.ToolInfo;
import software.onepiece.toolchain.service.ToolInstallService;
import software.onepiece.toolchain.service.ToolInstallServicesProvider;

import javax.inject.Inject;
import java.util.List;

public abstract class ToolUsingWorkAction<T extends ToolUsingWorkAction.Params> implements WorkAction<T> {
    public interface Params extends WorkParameters {
        Property<ToolInstallService> getToolInstall();
        ListProperty<String> getToolIds();
    }

    static public abstract class Default extends ToolUsingWorkAction<Params> { }

    protected List<ToolInfo> tools;

    @Inject
    protected abstract ObjectFactory getObjects();

    private ToolInstallServicesProvider toolServices() {
        return getObjects().newInstance(ToolInstallServicesProvider.class);
    }

    @Override
    public void execute() {
        this.tools = getParameters().getToolInstall().get().getTools(getParameters().getToolIds().get(), toolServices());
    }
}
