plugins {
    id("java-library")
}

java {
    registerFeature("test") {
        usingSourceSet(sourceSets.test.get())
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
