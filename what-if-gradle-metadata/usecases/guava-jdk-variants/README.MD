## What's the use case?

Guava publishes two variants: one for JDK6+ and one for JDK8+.
In an attempt to avoid both variants in a dependency graph, the "variant name" is encoded in the version.
Each Guava version is published twice: `28.1-android` (JDK6) and `28.1-jre` (JDK8)

## How is it solved?

In Gradle Module Metadata, arbitrary many _variants_ can be published explicitly.
Each variant can have individual dependencies and **artifacts**.
_Variant attributes_ are used to distinguish variants and can also be used to express that variants are mutually exclusive.
Gradle supports a set of [default attributes](https://docs.gradle.org/release-nightly/userguide/variant_attributes.html), including `org.gradle.jvm.version`.
Utilising that, Guava can now publish the following variants:

- `apiElements`: compile classpath dependencies, `org.gradle.jvm.version = 8`, artifact = `guava-28.1-jre.jar`
- `runtimeElements`: runtime classpath dependencies, `org.gradle.jvm.version = 8`, artifact = `guava-28.1-jre.jar`
- `jdk6ApiElements`: compile classpath dependencies, `org.gradle.jvm.version = 6`, artifact = `guava-28.1-android.jar`
- `jdk6RuntimeElements`: runtime classpath dependencies, `org.gradle.jvm.version = 6`, artifact = `guava-28.1-android.jar`

A build depending on Guava, will now automatically choose one, and only one, compatibly variant based on the `java.targetCompatibility` setting.
