plugins {
    `kotlin-dsl`
    id("maven-publish")
}
repositories {
    gradlePluginPortal()
}

group = "software.onepiece"
version = "1.0"

publishing {
    // If you have a Maven repository to publish to, you may use that here
    repositories.maven("https://example.com/repo/releases") {
        credentials {
            username = "jendrik"
            password = providers.environmentVariable("SUPER_SECRET").get()
        }
    }
}
