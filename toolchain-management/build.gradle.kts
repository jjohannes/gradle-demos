plugins {
    `java-gradle-plugin`
}

gradlePlugin.plugins.create("software.onepiece.toolchain-management") {
    id = name
    implementationClass = "software.onepiece.toolchain.ToolchainManagementPlugin"
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("net.lingala.zip4j:zip4j:2.11.5")
}