package software.onepiece.toolchain;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkAction;
import org.gradle.workers.WorkParameters;

import javax.inject.Inject;
import java.util.List;

public abstract class ToolUsingWorkAction<T extends ToolUsingWorkAction.Param> implements WorkAction<T> {
    public interface Param extends WorkParameters {
        Property<ToolInstallService> getToolInstall();
        ListProperty<String> getToolIds();
    }

    static public abstract class Default extends ToolUsingWorkAction<ToolUsingWorkAction.Param> { }

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
