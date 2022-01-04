plugins {
    id("java-library")
    id("groovy")
}

// activate sources variant (includes sources jar task and 'sourcesElements' configuration)
java.withSourcesJar()

configurations.named("sourcesElements") {
    // include sources of dependencies
    extendsFrom(configurations.implementation.get())
    outgoing.variants.create("sourcesDirectory") {
        attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "java-sources-directory")
        sourceSets.main.get().java.srcDirs.forEach {
            artifact(it)
        }
        sourceSets.main.get().groovy.srcDirs.forEach {
            artifact(it)
        }
    }
}

// create a 'sourcesPath' configuration to resolve sources
configurations.create("sourcesPath") {
    isCanBeConsumed = false
    extendsFrom(configurations.implementation.get())
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
    }
}

dependencies {
    implementation("org.codehaus.groovy:groovy:3.0.9")
}
