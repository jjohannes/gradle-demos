plugins {
    `java-gradle-plugin`
}

gradlePlugin.plugins.create("org.example.tool-installation") {
    id = name
    implementationClass = "org.example.ToolInstallationPlugin"
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.lingala.zip4j:zip4j:2.11.5")
}