plugins {
    id("java-gradle-plugin")
    `embedded-kotlin` // id("org.jetbrains.kotlin.jvm") version "<<version-embedded-in-gradle>>"
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
    id("com.gradle.plugin-publish") version "1.2.1" // if you do not publish to the Gradle Plugin Portal, you may remove this (but it also does not hurt)
}

group = "org.example"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
}

// plugins implemented as classes: we need to define IDs here
gradlePlugin {
    plugins.create("KMPAppPlugin") {
        id = "${project.group}.kmp-app"
        implementationClass = "${project.group}.${name}"
    }
    plugins.create("KMPLibPlugin") {
        id = "${project.group}.kmp-lib"
        implementationClass = "${project.group}.${name}"
    }
    // -- Plugin Portal Metadata (only needed if you publish to Gradle Plugin Portal) - run ':publishPlugins' task
    website = "https://example.org"
    vcsUrl = "https://example.org"
    plugins.all {
        tags = listOf("example")
    }
    // ---
}

// if you do not publish, the following is optional
version = "1.0"
publishing.repositories.maven("gradle-plugins-repository") // own repository to publish to (if needed) - run ':publish' task
