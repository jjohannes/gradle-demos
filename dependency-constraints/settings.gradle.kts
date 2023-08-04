pluginManagement {
    includeBuild("gradle-build-logic")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("app")
include("platform")