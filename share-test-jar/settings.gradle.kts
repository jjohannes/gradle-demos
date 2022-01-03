pluginManagement {
    includeBuild("gradle-build-logic")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("model", "service")
