rootProject.name = "java-17-modules-kotlin"

dependencyResolutionManagement {
    repositories.mavenCentral()
}

pluginManagement {
    includeBuild("gradle/plugins")
}

include("moduleone")
include("moduletwo")
include("app")
