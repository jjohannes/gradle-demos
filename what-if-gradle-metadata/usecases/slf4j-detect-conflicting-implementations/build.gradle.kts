plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-log4j12:1.7.28")

    implementation("org.slf4j:slf4j-simple:1.7.28")     // ! conflicting binding (conflict on capability 'org.slf4j:slf4j-binding:1.7.28')
    implementation("org.slf4j:log4j-over-slf4j:1.7.28") // ! conflicting log4j integration (conflict on capability 'org.slf4j:log4j-integration:1.7.28')
    implementation("log4j:log4j:1.2.17")                // ! conflicting log4j implementation (conflict on capability 'log4j:log4j:1.2.17')
}
