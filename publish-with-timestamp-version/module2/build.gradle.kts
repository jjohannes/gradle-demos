plugins {
    id("my-java-library")
}

dependencies {
    api("org.apache.commons:commons-lang3:3.14.0")

    implementation(project(":module1"))
}
