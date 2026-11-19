plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.6.0-M1")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.5.2") // should automatically align with core to 5.6.0-M1
}
