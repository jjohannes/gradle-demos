plugins {
    `java-library`
    `groovy`
}

java {
    withSourcesJar()
}

dependencies {
    implementation(localGroovy())
    implementation(project(":c"))
}