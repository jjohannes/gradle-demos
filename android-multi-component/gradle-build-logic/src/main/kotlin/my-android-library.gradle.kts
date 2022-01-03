plugins {
    id("com.android.library")
    id("maven-publish")
}

group = "example"
version = "1.0"

android {
    compileSdk = 29
    defaultConfig {
        minSdk = 16
        targetSdk = 29
    }
}

publishing {
    repositories.maven(File(rootDir, "repo"))

    afterEvaluate {
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