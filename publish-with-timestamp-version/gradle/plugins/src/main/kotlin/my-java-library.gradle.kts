plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.example.my-app"
version = rootProject.version // read version from root project

publishing {
    publications.create<MavenPublication>("lib").from(components["java"])
    repositories.maven("../test-repo")
}
