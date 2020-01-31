plugins {
    id("com.android.library") version "3.6.0-rc02" apply false
    kotlin("jvm") version "1.3.61" apply false
    kotlin("android") version "1.3.61" apply false
    kotlin("multiplatform") version "1.3.61" apply false
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

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}
