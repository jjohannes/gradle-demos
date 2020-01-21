plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    "internal"(platform(project(":platform")))

    implementation("org.apache.commons:commons-lang3")
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