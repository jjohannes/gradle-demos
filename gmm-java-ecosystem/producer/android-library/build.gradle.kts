plugins {
    id("com.android.library")
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
            dimension = "org.gradle.example.my-own-flavor"
        }
    }
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