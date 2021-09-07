### Use variant-aware dependency management to access source code of dependencies transitively

* Set up variants: [my-sources-aware-groovy-library.gradle.kts](build-logic/src/main/kotlin/my-sources-aware-groovy-library.gradle.kts)

* Access them (example): [main/build.gradle.kts](main/build.gradle.kts)

* Run example: `./gradlew :main:printAllSources`