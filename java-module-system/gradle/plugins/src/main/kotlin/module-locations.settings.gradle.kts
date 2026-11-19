import org.gradlex.javamodule.dependencies.initialization.JavaModulesExtension

// Configure the locations of where to find Java Modules – in the project hierarchy and in repositories
plugins {
    id("org.gradlex.java-module-dependencies")
}

configure<JavaModulesExtension> {
    directory("modules") {
        plugin("java-module")
        group = "org.example"
        module("app") {
            plugin("application")
            plugin("multi-target-tests")
        }
    }
    versions("gradle/versions")
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}
