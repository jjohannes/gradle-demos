plugins {
    id("com.android.library") version "8.0.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.20" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("org.jetbrains.kotlin.multiplatform") version "1.8.20" apply false
}

subprojects {
    group = "example"
    version = "1.0"

    apply(plugin = "maven-publish")
    the<PublishingExtension>().apply {
        repositories.maven(File(rootDir, "repo"))
    }
}
