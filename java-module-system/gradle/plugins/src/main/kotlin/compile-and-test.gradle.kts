// Tweak the Java building process (Java version, encoding, test memory settings, ...)
plugins {
    id("java")
    id("org.gradlex.java-module-testing")
    id("org.gradlex.java-module-packaging")
    id("org.gradlex.reproducible-builds")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(
        providers.fileContents(isolated.rootProject.projectDirectory
            .file("gradle/java-version.txt")).asText.get().trim()
    )
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
tasks.withType<Test>().configureEach {
    maxParallelForks = 4
    maxHeapSize = "1g"
}
testing.suites.withType<JvmTestSuite> {
    // remove automatically added compile time dependencies, as we define them explicitly
    configurations.getByName(sources.implementationConfigurationName) {
        withDependencies { removeIf { it.group == "org.junit.jupiter" && it.name == "junit-jupiter" } }
    }
    // Configure common test runtime dependencies for *all* projects
    dependencies { runtimeOnly("org.junit.jupiter:junit-jupiter-engine") }
}
