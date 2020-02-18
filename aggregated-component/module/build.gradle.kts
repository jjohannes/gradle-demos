plugins {
    `java-library`
    `maven-publish`
}

// === This could be a Plugin:
// - collect dependencies from subprojects and add them as 'implementation' to the published component
// - collect all class folders as input to JAR task
// 1) A configuration to resolve dependencies
val includedSubprojects: Configuration by configurations.creating {
    isCanBeConsumed = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}
// 2) extract dependencies, everything of the selected modules that is a dependency to an external module (ModuleComponentSelector)
fun findDependencies(component: DependencyResult) =
        if (component is ResolvedDependencyResult) {
            component.selected.dependencies.filter { resolved ->
                if (resolved is ResolvedDependencyResult) {
                    resolved.requested is ModuleComponentSelector
                } else {
                    throw RuntimeException(resolved.toString())
                }
            }.map { resolved ->
                (resolved as ResolvedDependencyResult).selected.moduleVersion!!
            }
        } else {
            throw RuntimeException(component.toString())
        }
// 3) add implementation dependencies to all external modules that were resolved as direct dependencies of any subproject
val jointImplementation = configurations.implementation.get()
jointImplementation.withDependencies {
    includedSubprojects.incoming.resolutionResult.root.dependencies.flatMap { findDependencies(it) }.forEach {
        jointImplementation.dependencies.add(dependencies.create(it.toString()))
    }
}
// 4) collect class folders in jar task (this should also add resources if needed)
tasks.jar {
    from(includedSubprojects.incoming.artifactView {
        attributes {
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.CLASSES))
        }
    }.files.filter { it.isDirectory }) // TODO this filter is not precise enough
}
// ====



group = "example"
version = "1.0"

dependencies {
    // Select which subprojects to "bundle" by depending on them like this:
    includedSubprojects(project(":fragment-module-a"))
    includedSubprojects(project(":fragment-module-b"))
}

publishing {
    repositories {
        maven {
            setUrl(File(rootDir, "repo"))
        }
    }
    publications {
        create<MavenPublication>("module") {
            from(components["java"])
        }
    }
}