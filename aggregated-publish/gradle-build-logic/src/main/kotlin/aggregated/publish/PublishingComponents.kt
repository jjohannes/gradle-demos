package aggregated.publish

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentSelector
import org.gradle.api.artifacts.component.ProjectComponentSelector
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.the

abstract class PublishingComponents(private val project: Project, private val softwareComponentFactory: SoftwareComponentFactory) {

    // use experimental API to ease setup of Configurations and Attributes
    val jvm = project.extensions.create(org.gradle.api.plugins.jvm.internal.JvmPluginExtension::class, "jvm", org.gradle.api.plugins.jvm.internal.DefaultJvmPluginExtension::class)

    fun register(library: String) {
        val bucket = "${library}Artifact"
        val resolver = jvm.createResolvableConfiguration("${library}ArtifactPath") {
            requiresJavaLibrariesAPI()
            usingDependencyBucket(bucket)
        }

        project.dependencies.add(bucket, project.project(":${library}"))
        val outgoingRuntime = jvm.createOutgoingElements("${library}RuntimeElements") {
            providesAttributes {
                runtimeUsage().library().asJar()
            }
            artifact(AggregatePublishArtifact(library, resolver))
        }
        val outgoingApi = jvm.createOutgoingElements("${library}ApiElements") {
            providesAttributes {
                apiUsage().library().asJar()
            }
            artifact(AggregatePublishArtifact(library, resolver))
        }

        val component = softwareComponentFactory.adhoc(library).apply {
            addVariantsFromConfiguration(outgoingRuntime) {
                mapToMavenScope("runtime")
            }
            addVariantsFromConfiguration(outgoingApi) {
                mapToMavenScope("compile")
            }
        }

        extractDependencies(resolver, outgoingRuntime)
        extractDependencies(resolver, outgoingApi) // TODO preserve API/impl separation

        val publish = project.the<PublishingExtension>()
        publish.publications.create<MavenPublication>(library) {
            // TODO this still creates one publication per component. Ultimately, we want a publishing task that publishes everything in one go.
            from(component)
            artifactId = library
        }
    }

    private fun extractDependencies(resolver: Configuration, outgoing: Configuration) {
        //TODO resolves during config time
        (resolver.incoming.resolutionResult.root.dependencies.first() as ResolvedDependencyResult).selected.dependencies.forEach {
            project.dependencies.add(outgoing.name, when (val d = it.requested) {
                is ModuleComponentSelector -> "${d.group}:${d.module}:${d.version}"
                is ProjectComponentSelector -> "${project.group}:${d.projectPath.substring(1)}:${project.version}"
                else -> throw UnsupportedOperationException()
            })
        }
    }

}