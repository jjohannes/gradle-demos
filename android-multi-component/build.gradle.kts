plugins {
    id("com.android.library") version "3.6.0-rc03" apply false
}

subprojects {
    apply(plugin = "maven-publish")

    repositories {
        google()
        jcenter()
    }

    group = "example"
    version = "1.0"

    extensions.getByType<PublishingExtension>().apply {
        repositories {
            maven {
                setUrl(File(rootDir, "repo"))
            }
        }
    }
}
