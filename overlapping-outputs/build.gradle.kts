plugins {
    id("application")
    id("code-gen")
}

application {
    mainClass = "org.example.App"
}

// https://scans.gradle.com/s/ujk25aibaly6o/timeline?cacheability=any_non_cacheable&hide-timeline&kind=task&outcome=success&sort=longest

// Reproduce surprising behavior:
// - ./gradlew run
// - delete '.gradle' folder in this project (Gradle looses knowledge about previous run)
// - ./gradlew run (task will run again with "Not cacheable: Overlapping outputs" message)

tasks.genCode {
    message = "Hello" // when exploring, modify this to avoid cache hits

    // projectDirectory: HandleStaleOutputsStep does NOT take care of removing stale files -> unexpected "Not cacheable: Overlapping outputs"
    destinationDir = layout.projectDirectory.dir("src/main/generated");

    // buildDirectory: HandleStaleOutputsStep does take care of removing stale files -> no unexpected "Not cacheable: Overlapping outputs"
    // destinationDir = layout.buildDirectory.dir("src/main/generated");
}
