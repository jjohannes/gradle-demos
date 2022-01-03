import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

fun Project.tests(projectDependency: ProjectDependency) =
    projectDependency.capabilities { requireCapability("${projectDependency.group}:${projectDependency.name}-test") }
