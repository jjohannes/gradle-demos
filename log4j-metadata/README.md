# How to patch Log4J to make -Xlint:all work

https://github.com/apache/logging-log4j2/issues/3437

Without the `jvmDependencyConflicts.patch { }` and `extraJavaModuleInfo { }` patch blocks
in [build.gradle.kts](build.gradle.kts) the build – `./gradlew assemble` – fails.