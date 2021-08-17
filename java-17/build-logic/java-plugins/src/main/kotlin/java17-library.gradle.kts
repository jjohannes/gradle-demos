plugins {
    id("java-library")
}

// Make sure Java 17 JDK is discovered by Gradle
// E.g. by setting 'org.gradle.java.installations.paths' in gradle.properties
// See also: https://docs.gradle.org/current/userguide/toolchains.html#sec:custom_loc
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}

tasks.test {
    useJUnitPlatform()
}