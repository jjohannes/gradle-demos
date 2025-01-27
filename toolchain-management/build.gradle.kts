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
    implementation("org.apache.commons:commons-compress:1.26.2")
    runtimeOnly("org.tukaani:xz:1.9")
}