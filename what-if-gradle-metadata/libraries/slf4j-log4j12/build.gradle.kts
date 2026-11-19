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

configurations {
    api {
        outgoing {
            capability("$group:${project.name}:$version")      // keep default capability to allow users to depend on the implementation directly
            capability("org.slf4j:slf4j-binding:$version")     // this is one binding for slf4j (only one is allowed)
            capability("org.slf4j:log4j-integration:$version") // this is one way of integrating log4j and slf4j (only one is allowed)
        }
    }
}

dependencies {
    api(platform("org.slf4j:slf4j-parent:$version"))

    api("org.slf4j:slf4j-api")
    implementation("log4j:log4j") // should this be implementation (i.e. scope RUNTIME)?
}

publishing {
    publications {
        create<MavenPublication>("slf4j") {
            from(components["java"])
        }
    }
}
