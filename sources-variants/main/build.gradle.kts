plugins {
    id("my-sources-aware-groovy-library")
}

dependencies {
    implementation(project(":lib-a"))
    implementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
}

tasks.register("printAllSources") {
    group = "help"
    description = "Prints all sources for demo purposes"
    doLast {
        println("== my own sources")
        configurations.sourcesElements.get().outgoing.artifacts.files.forEach {
            println(it.path)
        }
        println("== my dependency sources")
        configurations.sourcesPath.get().incoming.artifactView { lenient(true) }.files.forEach {
            println(it.path)
        }
    }
}

tasks.sourcesJar {
    from(configurations.sourcesPath.get().incoming.artifactView {
        attributes.attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, "java-sources-directory")
    }.files)
}