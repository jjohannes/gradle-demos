## Handle 'org.gradle.jvm.environment' conflicts in Gradle 6

Starting with Gradle 7, the [org.gradle.jvm.environment attribute](https://docs.gradle.org/current/userguide/variant_attributes.html#sub:jvm_default_attributes) is supported by Gradle out of the box.
In Gradle 6, you may have to add it, if you rely on newer versions of libraries that make use of it, like [Guava 32.1.3](https://github.com/google/guava/releases/tag/v32.1.0).

If you use `buildSrc` or `build-logic` already (genrally recommended) you can add it in your [Java convention plugin](buildSrc/src/main/groovy/org.example.java-common-conventions.gradle).

```
plugins {
    id 'java'
}

sourceSets.all {
    configurations.getByName(runtimeClasspathConfigurationName) {
        attributes.attribute(Attribute.of("org.gradle.jvm.environment", String), "standard-jvm")
    }
    configurations.getByName(compileClasspathConfigurationName) {
        attributes.attribute(Attribute.of("org.gradle.jvm.environment", String), "standard-jvm")
    }
}
```

If not, you can add it in your **root** [build.gradle](build.gradle).

```
allprojects {
    plugins.withId("java") {
        sourceSets.all {
            configurations.getByName(runtimeClasspathConfigurationName) {
                attributes.attribute(Attribute.of("org.gradle.jvm.environment", String), "standard-jvm")
            }
            configurations.getByName(compileClasspathConfigurationName) {
                attributes.attribute(Attribute.of("org.gradle.jvm.environment", String), "standard-jvm")
            }
        }
    }
}
```