rootProject.name = "java-17"

dependencyResolutionManagement {
    repositories.mavenCentral()
}

pluginManagement {
    includeBuild("build-logic")
}

include("lib")
