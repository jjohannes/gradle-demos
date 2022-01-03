plugins {
    id("java-library-with-test-jar")
}

dependencies {
    implementation(project(":model"))

    testImplementation(tests(project(":model")))
}
