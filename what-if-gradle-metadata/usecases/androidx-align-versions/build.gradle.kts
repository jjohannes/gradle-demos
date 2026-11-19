plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
    google()
}

dependencies {
    implementation("androidx.core:core-ktx:1.0.0") // should automatically align with core to 1.1.0
    implementation("androidx.core:core:1.1.0")
}
