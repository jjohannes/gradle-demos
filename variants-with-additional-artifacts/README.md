Use a component metadata rule to make Gradle download files that are not in the metadata of a component.
For example, the metadata files themselves.
[Here](gradle-build-logic/java-plugins/src/main/kotlin/org/example/gradle/metadatarules/DirectMetadataAccessVariantRule.kt) is a rule for that.

Run `./gradlew :downloadMetadata` to see it in action.
