package aggregated.publish

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.component.ModuleComponentSelector
import org.gradle.api.artifacts.component.ProjectComponentSelector
import org.gradle.api.artifacts.result.ResolvedDependencyResult
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.the

abstract class PublishingComponents(private val project: Project, private val softwareComponentFactory: SoftwareComponentFactory) {

    fun register(library: String) {
        val bucket = project.configurations.create("${library}Artifact") {
            isCanBeConsumed = false
            isCanBeResolved = false
        }
        val resolver = project.configurations.create("${library}ArtifactPath") {
            isCanBeConsumed = false
            isCanBeResolved = true

            attributes.attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))

            extendsFrom(bucket)
        }

        project.dependencies.add(bucket.name, project.project(":${library}"))
        val outgoingRuntime = project.configurations.create("${library}RuntimeElements") {

            attributes.attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_RUNTIME))
            attributes.attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
            attributes.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))

            outgoing.artifact(AggregatePublishArtifact(library, resolver))
        }
        val outgoingApi = project.configurations.create("${library}ApiElements") {

            attributes.attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage.JAVA_API))
            attributes.attribute(Category.CATEGORY_ATTRIBUTE, project.objects.named(Category.LIBRARY))
            attributes.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, project.objects.named(LibraryElements.JAR))

            outgoing.artifact(AggregatePublishArtifact(library, resolver))
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