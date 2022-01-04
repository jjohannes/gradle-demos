plugins {
    id("java-library-with-internal-platform-support")
}

dependencies {
    internal(platform(project(":platform")))

    implementation("org.apache.commons:commons-lang3")
}
