// Combine all aspects to build a Java Module in this project
plugins {
    id("java-library")
    id("compile-and-test")
    id("dependency-analysis")
    id("metadata-patch")
    id("targets")
}

// Version of our own modules is stored in 'version.txt'
version = providers.fileContents(isolated.rootProject.projectDirectory
    .file("gradle/version.txt")).asText.get().trim()

// Central version management for 3rd party modules
jvmDependencyConflicts {
    consistentResolution {
        platform(":versions")
        providesVersions(":app")
    }
}

// If application, expect application class: <module-name>.App
plugins.withId("application") {
    configure<JavaApplication> { mainClass.set(mainModule.map { name -> "$name.App" }) }
}
