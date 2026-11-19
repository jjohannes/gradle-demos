plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "androidx.core"
version = findProperty("androidxVersion")?: "1.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_6
    targetCompatibility = JavaVersion.VERSION_1_6
}

tasks.withType<Jar> {
    archiveExtension.set("aar")
    extra["repoUrl"] = "https://dl.google.com/dl/android/maven2"
}

dependencies {
    constraints {
        // core-ktx, if present, should align to my version
        api("androidx.core:core-ktx:${project.version}")
    }
}

publishing {
    publications {
        create<MavenPublication>("androidx-core") {
            from(components["java"])
            pom {
                packaging = "aar"
            }
        }
    }
}
