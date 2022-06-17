plugins {
    id("java-library")
}

repositories.ivy {
    name = "Direct File Download"
    // Base URL
    url = project.uri("https://raw.githubusercontent.com/jjohannes")
    // Look for the file directly as there is no metadata
    metadataSources.artifact()
    patternLayout {
        // Map 'organisation:module:revision' coordinates from the dependency definition to the file location
        // Note: you may remove [revision] if the file is not versioned, in that case make sure the dependency
        //       is marked as 'changing'. Otherwise, the first version downloaded will be cached forever.
        artifact("[organisation]/[revision]/[module](.[ext])")
        ivy("[organisation]/[revision]/ivy.xml") // point to a location that does not exist (otherwise Gradle attempts to read the artifact as ivy.xml)
        setM2compatible(true) // organisation maps to folder structure 'a.b.c' -> a/b/c
    }
    content {
        onlyForConfigurations("custom")
    }
}

val custom = configurations.create("custom") {
    resolutionStrategy.cacheChangingModulesFor(1, "days") // How long to cache the file which may change
}

dependencies {
    custom("understanding-gradle:README:main@MD") {
        isChanging = true
    }
    custom("gradle-plugins-howto:README:main@md") {
        isChanging = true
    }
}

tasks.register("printFiles") {
    doLast {
        custom.files.forEach {
            println("${it.name}:\n${it.readText().substring(0, 30)}...\n\n")
        }
    }
}
