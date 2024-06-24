pluginManagement {
    includeBuild("../plugins")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

include("app", "lib")
