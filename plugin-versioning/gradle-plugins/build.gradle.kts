plugins {
    `kotlin-dsl`
    `maven-publish`
}

version = "0.2"

group = "com.example.product.build-system"

dependencies {
    implementation("org.gradlex:java-module-dependencies:1.3.1")
}

publishing {
    repositories.maven("repo")
}