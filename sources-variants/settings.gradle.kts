pluginManagement {
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("lib-a", "lib-b", "main")