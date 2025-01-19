plugins {
    `java-gradle-plugin`
}

group = "software.onepiece.gradle"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(11)
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