plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:3.20.4")
    implementation("com.gradle:develocity-gradle-plugin:4.6.1")
    implementation("org.gradlex:extra-java-module-info:1.14.2")
    implementation("org.gradlex:java-module-dependencies:1.13.2")
    implementation("org.gradlex:java-module-packaging:1.3.1")
    implementation("org.gradlex:java-module-testing:1.8.1")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.5")
    implementation("org.gradlex:reproducible-builds:1.1")
}
