plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "29.0.2"
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("releasePublication") {
                from(components["release"])
            }
            create<MavenPublication>("debugPublication") {
                from(components["debug"])

                artifactId = "${project.name}-debug"
            }
        }
    }
}