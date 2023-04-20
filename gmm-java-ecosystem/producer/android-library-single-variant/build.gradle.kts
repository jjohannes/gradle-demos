plugins {
    id("com.android.library")
}

android {
    namespace = "example.androidlibsingle"
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
            dimension = "org.gradle.example.my-own-flavor"
        }
    }
    publishing {
        singleVariant("fullRelease")
    }
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            afterEvaluate {
                from(components["fullRelease"])
            }
        }
    }
}