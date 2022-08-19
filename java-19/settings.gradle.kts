pluginManagement {
    includeBuild("gradle/plugins")
}
dependencyResolutionManagement {
    repositories.mavenCentral()
}

rootProject.name = "java-19"

include("lib")
