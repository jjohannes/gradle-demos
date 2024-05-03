plugins {
    `embedded-kotlin`
    `java-gradle-plugin`
    `maven-publish`
}

repositories.gradlePluginPortal()

group = "custom"
version = "1.0"

gradlePlugin.plugins.create("custom-build-system") {
    id = "custom-build-system"
    implementationClass = "custom.buildsystem.CustomBuildSystemPlugin"
}

publishing.repositories.maven("../plugin-repo")
