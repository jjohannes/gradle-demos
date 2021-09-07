plugins {
    id("my-sources-aware-groovy-library")
}

dependencies {
    implementation(project(":lib-b"))
}