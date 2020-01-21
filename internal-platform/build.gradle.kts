subprojects {
    repositories {
        mavenCentral()
    }

    group = "org.example"
    version = "1.0"

    plugins.withType<JavaLibraryPlugin> {
        val internal by configurations.creating {
            isVisible = false
            isCanBeConsumed = false
            isCanBeResolved = false
        }
        configurations["compileClasspath"].extendsFrom(internal)
        configurations["runtimeClasspath"].extendsFrom(internal)
        configurations["testCompileClasspath"].extendsFrom(internal)
        configurations["testRuntimeClasspath"].extendsFrom(internal)
    }
}
