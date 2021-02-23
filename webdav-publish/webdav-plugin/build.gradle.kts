plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.maven.wagon:wagon:3.4.2")
    implementation("org.apache.maven.wagon:wagon-webdav-jackrabbit:3.4.2")
}