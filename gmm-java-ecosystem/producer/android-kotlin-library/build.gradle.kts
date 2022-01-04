plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 29
    defaultConfig {
        minSdk = 16
        targetSdk = 29
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("lib") {
                from(components["all"])
            }
        }
    }
}