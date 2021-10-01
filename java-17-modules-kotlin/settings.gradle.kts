rootProject.name = "java-17-modules-kotlin"

dependencyResolutionManagement {
    repositories.mavenCentral()
}

pluginManagement {
    includeBuild("build-logic")
}

include("moduleone")
include("moduletwo")
include("app")
