plugins {
    id("com.android.library") version "7.0.4" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
    id("org.jetbrains.kotlin.android") version "1.6.10" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.6.10" apply false
}

subprojects {
    group = "example"
    version = "1.0"

    apply(plugin = "maven-publish")
    the<PublishingExtension>().apply {
        repositories.maven(File(rootDir, "repo"))
    }
}
