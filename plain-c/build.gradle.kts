plugins {
    id("com.gradle.plugin-publish") version "1.2.0"
}

group = "software.onepiece.gradle"
version = "0.1"

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins.create(project.name) {
        id = "software.onepiece.${project.name}"
        implementationClass = "software.onepiece.gradle.plainc.PlainCPlugin"
        displayName = "Individualised C development with Gradle"
        description = "Allows you to directly use Gradle's native C compile/assemble/link tasks with custom executables"
        vcsUrl.set("https://github.com/jjohannes/plain-c")
        website.set(vcsUrl)
        tags.addAll(listOf("native", "c"))
    }
}
