pluginManagement {
    includeBuild("gradle-build-logic")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("lib-a")
include("lib-b")
include("platform")