plugins {
    `java-library`
}

dependencies {
    implementation(project(":fragment-module-b"))
    implementation("com.google.guava:guava:28.1-jre")
}