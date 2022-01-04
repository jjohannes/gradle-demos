pluginManagement {
    repositories.gradlePluginPortal()
    repositories.google()
}

dependencyResolutionManagement {
     repositories.maven(File(rootDir.parentFile, "producer/repo"))
     repositories.mavenCentral()
     repositories.google()
}

include("java-app")
include("kotlin-app")
include("native-app")
include("android-app")
include("android-kotlin-app")