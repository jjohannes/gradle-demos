plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.8") // should automatically align with core to 2.9.9
    // jackson-annotations stay on 2.9.0 as defined in the the bom: https://repo1.maven.org/maven2/com/fasterxml/jackson/jackson-bom/2.9.9/jackson-bom-2.9.9.pom
}
