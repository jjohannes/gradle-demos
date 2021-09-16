plugins {
    id("java-library")
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    testRuntimeOnly(project(":test-utils"))
}

tasks.test {
    useJUnitPlatform {
        includeEngines("classloader-isolation-junit-vintage")
        // includeEngines("junit-vintage")
    }

    outputs.upToDateWhen { false } // to always run the tests for experimentation purpose
}