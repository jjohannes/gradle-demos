pluginManagement {
    includeBuild("gradle-build-logic")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("publish")
include("list", "utilities")
