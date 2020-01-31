plugins {
    id("com.android.application") version "3.6.0-rc02" apply false
    kotlin("jvm") version "1.3.61" apply false
    kotlin("android") version "1.3.61" apply false
    kotlin("android.extensions") version "1.3.61" apply false
    kotlin("multiplatform") version "1.3.61" apply false
}

allprojects {
    repositories {
        maven {
            setUrl(File(rootDir.parentFile, "producer/repo"))
        }
        jcenter()
        google()
    }
}
