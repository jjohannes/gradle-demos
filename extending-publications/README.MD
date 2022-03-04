## Extending a publication

- With more 'artifacts' as extra 'variants' with metadata (so they may have dependencies etc.)
- With informational metadata in the POM files (like license etc.)

### How?

Things are configured in a convention plugin:

- [java-library-with-extras.gradle.kts](gradle-build-logic/java-plugins/src/main/kotlin/java-library-with-extras.gradle.kts)

And then used in two projects:

- [library1/build.gradle.kts](library1/build.gradle.kts)
- [library2/build.gradle.kts](library2/build.gradle.kts)

Publish the libraries by calling:

- `./gradlew publish`

Explore the produced files and metadata. E.g.:

- `extending-publications/example-repo/com/example/library1/1.0`
- `extending-publications/example-repo/com/example/library1/1.0/library1-1.0.module`
- `extending-publications/example-repo/com/example/library1/1.0/library1-1.0.pom`

There is a separate small Gradle project showing how to get the 'readme' variant from the published libraries:

- [example-library-user/build.gradle.kts](example-library-user/build.gradle.kts)
- `../gradlew printReadmes`
