pluginManagement {
    repositories.gradlePluginPortal()
    repositories.google()
}
dependencyResolutionManagement {
    repositories.mavenCentral()
    repositories.google()
}

include("java-library")
include("android-library")
include("android-library-single-variant")
include("android-kotlin-library")
include("kotlin-library")
include("kotlin-multiplatform-library")
include("kotlin-multiplatform-android-library")