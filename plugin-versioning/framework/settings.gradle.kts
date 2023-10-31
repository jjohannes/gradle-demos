pluginManagement {
    // includeBuild("../gradle-plugins")
    repositories.maven("../gradle-plugins/repo")
    repositories.gradlePluginPortal()
}

plugins {
    id("com.example.product.settings") version "0.1"
}

include("main")
include("component.a")
include("component.b")
