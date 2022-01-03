plugins {
    id("my-android-library")
}

configurations.all {
    resolutionStrategy {
        // To behave differently when resolving the local project (where both variants are in the same component)
        dependencySubstitution {
            substitute(module("example:module-a")).with(project(":module-a"))
            substitute(module("example:module-a-debug")).with(project(":module-a"))
        }
    }
}

dependencies {
    debugApi("example:module-a-debug:1.0")
    releaseApi("example:module-a:1.0")
}
