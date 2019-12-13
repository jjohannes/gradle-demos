plugins {
    `java-library`
}

java {
    withSourcesJar()
}

dependencies {
    implementation(project(":b"))
}

tasks.create("allSources") {
    doLast {
        println("== my own sources")
        configurations["sourcesElements"].outgoing.artifacts.files.forEach {
            println(it.name)
        }
        println("== my dependency sources")
        configurations["sourcesPath"].files.forEach {
            println(it.name)
        }
    }
}