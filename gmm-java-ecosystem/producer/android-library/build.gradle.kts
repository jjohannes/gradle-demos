plugins {
    id("com.android.library")
}

android {
    namespace = "example.androidlib"
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
        multipleVariants {
            allVariants()
        }
    }
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