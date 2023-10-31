plugins {
    id("com.example.product.base")
    id("java-library")
    id("maven-publish")
}

group = "com.example.product.framework"

version = providers.fileContents(rootProject.layout.projectDirectory.file("version.txt")).asText.get().trim()

publishing {
    publications.create<MavenPublication>("lib").from(components["java"])
    repositories.maven("../repo")
}
