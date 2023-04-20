import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "example.androidkotlinlib"
    compileSdk = 33
    defaultConfig {
        minSdk = 16
    }
    publishing {
        multipleVariants {
            allVariants()
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(kotlin("stdlib"))
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            afterEvaluate {
                from(components["default"])
            }
        }
    }
}