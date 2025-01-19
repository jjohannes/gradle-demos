package software.onepiece.toolchain;

import org.gradle.api.Task;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.services.ServiceReference;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;

import javax.inject.Inject;

public interface ToolUsingTask extends Task {
    @Input
    ListProperty<String> getToolIds();

    @ServiceReference("toolInstall")
    Property<ToolInstallService> getToolInstall();

    @Inject
    ObjectFactory getObjects();

    default ToolInstallServicesProvider toolServices() {
        return getObjects().newInstance(ToolInstallServicesProvider.class);
    }
}
