import com.example.gradle.ReadmeConvert

plugins {
    id("maven-publish")
    id("java-library")
    // Publish a 'test fixture' Jar. Gradle does all the setup for us, when we say:
    id("java-test-fixtures")
}

// Group and Version for the published component's coordinates
group = "com.example"
version = "1.0"

// Publish the sources Jars. Gradle does all the setup for us when we say:
java.withSourcesJar()

// Publish an addition Jar with code from the 'extraFeature' source set. Gradle does all the setup for us, when we say:
val extraFeature = sourceSets.create("extraFeature")
java.registerFeature(extraFeature.name) {
    usingSourceSet(extraFeature)

    // Also want the sources for the extra feature? Here you go:
    withSourcesJar()
}

// If you need something more individual, you can do what Gradle is doing for you in the other cases step-by-step like this:

    // A file to publish
    val readmeMd = layout.projectDirectory.file("README.MD")

    // Or a task that is producing a file to publish
    val readmeConvert = tasks.register<ReadmeConvert>("readmeConvert") {
        fromMd.set(readmeMd)
        toTxt.set(layout.buildDirectory.file("converted/readme.txt"))
    }

    // The key to this is a 'consumable' configuration that represent our additional 'variant'
    val readmeElements = configurations.create("readmeElements") {
        isCanBeConsumed = true
        isCanBeResolved = false

        // You need attributes to distinguish the variant from others
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
            attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("readme"))
        }

        // You can add dependencies to this variant by extending existing configuration:
        extendsFrom(configurations.implementation.get())
        // Or by defining individual dependencies for 'readmeMdElements' in the 'dependencies {}' block

        // If you want your variant to be selectable IN ADDITION (and not only as alternative) to other variants, it would need a different capability:
        // outgoing.capability("${project.group}:${project.name}-readme:${project.version}")

        // And of course, your 'variant' needs the artifact(s).
        // Note: You can add multiple. They will all be downloaded together if the variant is selected.
        outgoing.artifact(readmeMd)
        outgoing.artifact(readmeConvert) // We add a task - Gradle will execute it first and then the task output will be used.
    }

    // Finally, we need to make out new 'variant' part of the 'java component' that we publish (see 'publishing {}' part below)
    (components["java"] as AdhocComponentWithVariants).addVariantsFromConfiguration(readmeElements) {
        // We can tell here how dependencies that only exist in this variant are represented in the POM.
        // The POM has no variants, but only ONE flat list of dependencies with a fixed set of scopes.
        // Let's just use optional which is ony 'informative metadata' that Maven does not read.
        // For Gradle, this does not matter as it reads dependency information from the Gradle Metadata.
        mapToOptional()
    }


publishing {
    publications.create<MavenPublication>("library") {
        // The publication is created from the 'java component' and gets all information from there
        from(components["java"])

        // Only informational metadata (i.e. metadata not needed by Gradle to understand the component) is defined here (if needed)
        pom.inceptionYear.set("2022")
        pom.licenses {
            license {
                name.set("United Federation of Planets License")
                url.set("https://www.wikidata.org/wiki/Q1110")
            }
        }
        pom.scm {
            url.set("https://github.com/jjohannes/gradle-demos")
        }

        // Gradle will warn that not all information about the additional 'variants' is represented in the POM - because it's not possible
        // We ignore the warnings, because we are aware of that. All information is in the Gradle Metadata.
        suppressAllPomMetadataWarnings()
    }

    // Repository to publish to
    repositories.maven("../example-repo")
}


