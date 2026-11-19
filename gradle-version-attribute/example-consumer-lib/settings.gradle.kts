pluginManagement {
    repositories {
        maven { url = uri("../local-plugin-repo") }
        gradlePluginPortal()
    }
}

include("lib")