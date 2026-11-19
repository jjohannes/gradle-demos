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
}

java {
    targetCompatibility = JavaVersion.VERSION_1_6 // <- increase to 8 or higher get the 'jre' instead of the 'android' jar, decrease to fail
}
