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

configurations.all {
    resolutionStrategy {
        dependencySubstitution {
            substitute(module("example:module-a")).with(project(":module-a"))
            substitute(module("example:module-a-debug")).with(project(":module-a"))
        }
    }
}

dependencies {
    debugApi("example:module-a-debug:1.0")
    releaseApi("example:module-a:1.0")
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