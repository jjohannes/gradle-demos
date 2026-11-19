plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.13.0" // need upcoming release to actually being able to publish to Plugin Portal
}

repositories {
    gradlePluginPortal()
}

group = "com.example"
version = "1.0"

val gradlePluginApiVersionAttribute = Attribute.of("org.gradle.plugin.api-version", String::class.java) // running on Gradle 6, can't yet access GradlePluginApiVersion.GRADLE_PLUGIN_API_VERSION_ATTRIBUTE
val gradle7 = sourceSets.create("gradle7")
java {
    registerFeature(gradle7.name) {
        usingSourceSet(gradle7)
        capability(project.group.toString(), project.name, project.version.toString())
    }
}
configurations.configureEach {
    if (isCanBeConsumed && name.startsWith(gradle7.name))  {
        attributes {
            attribute(gradlePluginApiVersionAttribute, "7.0")
        }
    }
}
tasks.named<Copy>(gradle7.processResourcesTaskName) {
    val copyPluginDescriptors = rootSpec.addChild()
    copyPluginDescriptors.into("META-INF/gradle-plugins")
    copyPluginDescriptors.from(tasks.pluginDescriptors)
}

dependencies {
    "gradle7CompileOnly"(gradleApiFor("7.0-milestone-3"))
    "gradle7CompileOnly"(localGroovy()) // to accommodate the Groovy 2->3 switch in Gradle 7
}

gradlePlugin {
    plugins.create("greeting") {
        id = "com.example.greeting"
        implementationClass = "example.plugin.GreetingPlugin"
    }
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../../local-plugin-repo")
        }
    }
}

fun gradleApiFor(version: String) =
        files("${System.getProperty("user.home")}/.gradle/caches/$version/generated-gradle-jars/gradle-api-$version.jar")
