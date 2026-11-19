plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "org.slf4j"
version = findProperty("slf4jVersion")?: "1.7.28"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(platform("org.slf4j:slf4j-parent:$version"))
}

publishing {
    publications {
        create<MavenPublication>("slf4j") {
            from(components["java"])
        }
    }
}
