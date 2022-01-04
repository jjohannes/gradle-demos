plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

android {
    compileSdk = 29
    defaultConfig {
        minSdk = 16
        targetSdk = 29
    }
    flavorDimensions.add("org.gradle.example.my-own-flavor")
    productFlavors {
        create("demo") {
            dimension = "org.gradle.example.my-own-flavor"
        }
        create("full") {
            dimension= "org.gradle.example.my-own-flavor"
        }
    }

}

kotlin {
    // jvm()
    js()
    macosX64()
    linuxX64()
    android {
        publishLibraryVariants("fullRelease", "demoRelease",
                "fullDebug", "demoDebug")
    }
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
    "androidMainImplementation"(kotlin("stdlib"))
    "jsMainImplementation"(kotlin("stdlib-js"))
}
