pluginManagement {
    // !! Maybe it only works when plugins are published, but from source with 'includeBuild' should also work
    // includeBuild("../plugin")
    repositories.maven("../plugin-repo")
}
plugins {
    id("custom-build-system") version "1.0"
}