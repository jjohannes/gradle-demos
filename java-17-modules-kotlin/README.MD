# Build Java Modules (JPMS) written in Kotlin using Java 17

To try out, run:

`./gradlew :app:run`

or

`./gradlew :app:check`

**Note:** if yu get the following error

```
> Task :moduletwo:compileKotlin FAILED
e: Module com.fasterxml.jackson.databind cannot be found in the module graph
```

Go to `java-17-modules-kotlin/moduletwo/src/main/java/module-info.java` and comment out `requires com.fasterxml.jackson.databind;`.
Then run the build until it fails. Then revert the change and build again.
(I assume this is some bug related to how the `module-info.java` is processed by the Kotlin compiler.)