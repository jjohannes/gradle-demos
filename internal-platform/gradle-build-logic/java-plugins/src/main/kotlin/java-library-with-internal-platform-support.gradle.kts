plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.example"
version = "1.0"

val internal = configurations.create("internal") {
    isCanBeConsumed = false
    isCanBeResolved = false
}

configurations.compileClasspath.get().extendsFrom(internal)
configurations.runtimeClasspath.get().extendsFrom(internal)
configurations.testCompileClasspath.get().extendsFrom(internal)
configurations.testRuntimeClasspath.get().extendsFrom(internal)

publishing {
    publications.create<MavenPublication>("library") {
        from(components["java"])
        versionMapping {
            usage("java-api") { fromResolutionResult() }
            usage("java-runtime") { fromResolutionResult() }
        }
    }

    repositories.maven(File(rootProject.rootDir, "repo"))
}
