plugins {
    `java-library`
    `groovy`
}

java {
    withSourcesJar()
}

dependencies {
    implementation(localGroovy())
    implementation(project(":b"))
}

tasks.create("allSources") {
    doLast {
        println("== my own sources")
        configurations["sourcesElements"].outgoing.artifacts.files.forEach {
            println(it.path)
        }
        println("== my dependency sources")
        configurations["sourcesPath"].files.forEach {
            println(it.path)
        }
    }
}

tasks.named<Jar>("sourcesJar") {
    from(configurations["sourcesPath"].incoming.artifactView {
        attributes.attribute(Attribute.of("artifactType", String::class.java), "java-sources-directory")
    }.files)
}