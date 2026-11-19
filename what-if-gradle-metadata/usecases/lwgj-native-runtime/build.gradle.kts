import org.gradle.nativeplatform.OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE
import org.gradle.nativeplatform.OperatingSystemFamily.*
import org.gradle.nativeplatform.MachineArchitecture.ARCHITECTURE_ATTRIBUTE

plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
}

configurations.runtimeClasspath.get().attributes {
    // select a platform, will fail to compose a runtime classpath if non is selected
    attribute(OPERATING_SYSTEM_ATTRIBUTE, objects.named(WINDOWS)) // or MACOS or LINUX
    attribute(ARCHITECTURE_ATTRIBUTE,     objects.named("x86"))   // or x86-64 or arm32 or arm64
}
dependencies {
    implementation("org.lwjgl:lwjgl:3.2.3")
}
