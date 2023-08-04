# Dependency Constraints in Gradle

Sample of how to define dependency constraints for **all** your dependencies
(including transitive ones) in a platform projects with dependency constraints.
It includes a metadata rule that strips the versions from transitive dependencies
so that Gradle fails if a constraint is missing.

```
./gradlew :app:run
```

Remove a constraint from `platform/build.gradle.kts` to see the build fail.
