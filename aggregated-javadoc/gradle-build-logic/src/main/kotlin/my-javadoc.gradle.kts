plugins {
    id("java")
}

val sourcesPath = configurations.create("sourcesPath") {
    isCanBeResolved = true
    isCanBeConsumed = false
    extendsFrom(configurations.implementation.get())
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named("java-sources"))
    }
}

tasks.register<Javadoc>("fullJavadoc") {
    source = sourcesPath.asFileTree
    classpath = configurations.compileClasspath.get()
}