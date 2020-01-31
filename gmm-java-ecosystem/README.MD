# Demonstrates how Gradle Module Metadata is used in the extended Java ecosystem

What additional information do the Android and Kotlin plugins publish in `.module` files and how can that be used?

To publish sample libraries (locally to `producer/repo`)

```
cd producer
./gradlew publish
```

Try out some samples in `consumer`:

```
cd consumer

# Java or Kotlin application using JVM libraries built with different plugins 
./gradlew java-app:run
./gradlew kotlin-app:run

# Android app using JVM libraries built with different plugins 
./gradlew android-app:installFullDebug
./gradlew android-kotlin-app:installFullDebug

# A native App build with Kotlin multiplatform
./gradlew native-app:runReleaseExecutableMacosX64
```
