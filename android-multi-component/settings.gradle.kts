pluginManagement {
    includeBuild("gradle-build-logic")
    repositories.gradlePluginPortal()
    repositories.google()
}
dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.google()
}

include("module-a")
include("module-b")