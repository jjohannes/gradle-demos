plugins {
    id("java-library-with-extras")
}

dependencies {
    implementation(project(":library2"))
    // Or, if this should be a 'readme-only' dependency: readmeElements(project(":library2"))
}

