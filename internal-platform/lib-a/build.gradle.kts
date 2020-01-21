plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    "internal"(platform(project(":platform")))

    api("com.google.guava:guava")
    api(project(":lib-b"))
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionResult()
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("file://${rootProject.buildDir}/repo")
        }
    }
}
