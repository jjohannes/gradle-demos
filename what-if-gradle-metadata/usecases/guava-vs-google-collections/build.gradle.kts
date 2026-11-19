plugins {
    `java-library`
}

repositories {
    // maven { setUrl(rootProject.file("../repository")) } <-- use this instead of 'mavenLocal()' if you do not use the real Guava fork
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:28.1-jre")
    implementation("com.google.collections:google-collections:1.0") // this will be a conflict, resolve by removing, or by strategy (if brought in transitively)
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
        // selectHighestVersion()
    }
}
