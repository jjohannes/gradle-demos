## Reproducer for Build Scan issue with Settings Plugin implemented in Java

The settings plugin is using `pluginManagement.includeBuild` to include the `project-plugins` build.
This causes an issue when the Settings Plugin is implemented as a Java Class.

- Run `./gradlew build` ->
  - ❌ No build scan - `This build uses Gradle features that are incompatible with build scans (code: late-load-build-op).`  
- Change [settings.gradle.kts](settings.gradle.kts): `id("my-smart-settings-plugin-java-impl")` -> `id("my-smart-settings-plugin-groovy-dsl")`
  - 👍 Build Scan is produced . 