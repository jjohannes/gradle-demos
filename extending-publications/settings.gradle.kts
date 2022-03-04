pluginManagement {
    includeBuild("gradle-build-logic")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("library1")
include("library2")
