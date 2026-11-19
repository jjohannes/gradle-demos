import org.gradlex.javamodule.moduleinfo.ExtraJavaModuleInfoPluginExtension
import org.gradlex.javamodule.packaging.JavaModulePackagingExtension
import org.gradlex.jvm.dependency.conflict.resolution.JvmDependencyConflictsExtension

plugins {
    id("org.gradlex.java-module-dependencies") version "1.13.2"
    id("org.gradlex.extra-java-module-info") version "1.14.2" apply false
    id("org.gradlex.java-module-packaging") version "1.3" apply false
    id("org.gradlex.java-module-testing") version "1.8.1" apply false
    id("org.gradlex.jvm-dependency-conflict-resolution") version "2.5" apply false
    id("com.autonomousapps.build-health") version "3.17.0"
}

rootProject.name = "starfleet-database"

javaModules {
    directory("modules") {
        group = "starfleet.db"
        plugin("java-library")
        plugin("org.gradlex.jvm-dependency-conflict-resolution")
        plugin("org.gradlex.extra-java-module-info")
        plugin("org.gradlex.java-module-testing")
        module("starfleet-db-app") {
            plugin("application")
            plugin("org.gradlex.java-module-packaging")
        }
        versions("versions")
    }
}

gradle.lifecycle.beforeProject {
    plugins.withId("java") {
        dependencies {
            "implementation"(platform(project(":versions")))
        }
        configure<JavaPluginExtension> {
            toolchain.languageVersion = JavaLanguageVersion.of(27)
        }
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs = listOf("-Werror", "-Xlint:all,-module,-missing-explicit-ctor")
        }
        configure<JvmDependencyConflictsExtension> {
            patch {
                align("dev.tamboui:tamboui-toolkit", "dev.tamboui:tamboui-jline3-backend")
            }
        }
        configure<ExtraJavaModuleInfoPluginExtension> {
            failOnAutomaticModules = true
            module("org.jline:jline", "org.jline") {
                requires("java.logging")
            }
        }
    }
    plugins.withId("application") {
        configure<JavaApplication> {
            version = "1.0.0"
            mainClass = "starfleet.db.app.StarfleetDatabase"
            applicationDefaultJvmArgs = listOf("--enable-native-access=org.jline")
        }
        configure<JavaModulePackagingExtension> {
            applicationName = "StarfleetDB"
        }
    }
}

dependencyResolutionManagement {
    repositories.mavenCentral()
}