pluginManagement {
    includeBuild("gradle-build-logic")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("module1")
include("module2")
include("aggregator")
