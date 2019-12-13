plugins {
    `java-library`
}

java {
    withSourcesJar()
}

dependencies {
    implementation(project(":c"))
}