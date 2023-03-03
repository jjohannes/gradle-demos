pluginManagement {
    includeBuild("gradle/plugins")
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}

rootProject.name = "java-19"

include("lib")
