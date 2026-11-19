plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "com.fasterxml.jackson.core"
version = findProperty("jacksonVersion")?: "2.9.9"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(platform("com.fasterxml.jackson:jackson-bom:$version"))
}

publishing {
    publications {
        create<MavenPublication>("jackson") {
            from(components["java"])
        }
    }
}
