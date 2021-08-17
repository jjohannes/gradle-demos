plugins {
    // Put java toolchain config in a local convention plugin to use it in all (sub)projects
    // See: build-logic/java-plugins/src/main/kotlin/java17-library.gradle.kts
    id("java17-library")
}
