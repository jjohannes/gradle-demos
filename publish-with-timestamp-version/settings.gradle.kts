pluginManagement {
    includeBuild("gradle/plugins")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("module1")
include("module2")
