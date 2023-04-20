import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

android {
    namespace = "example.kotlinlibmpandroid"
    compileSdk = 33
    defaultConfig {
        minSdk = 16
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
    js(IR) {
        browser()
    }
    macosX64()
    linuxX64()
    android {
        publishLibraryVariants("fullRelease", "demoRelease",
                "fullDebug", "demoDebug")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
    "androidMainImplementation"(kotlin("stdlib"))
    "jsMainImplementation"(kotlin("stdlib-js"))
}
