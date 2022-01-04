plugins {
    id("java-library-with-internal-platform-support")
}

dependencies {
    internal(platform(project(":platform")))

    api("com.google.guava:guava")
    api(project(":lib-b"))
}
