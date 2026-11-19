# Plain C - Gradle plugin

An experimental plugin for customized **C** development with Gradle.
It showcases how to use tasks that are provided by Gradle core for **C** development,
such as `org.gradle.language.c.tasks.CCompile`, directly as part of a custom build system.
This can make sense in a highly customized Gradle build system where specific (and multiple) **C** compilation/assembling/linking
steps are part of a larger build pipeline with other custom Gradle tasks.

The plugin provides an extension called `plainc` to:

- Set the `targetPlatform` property of a task to a generic platform via `targetPlatform.set(plainc.platform())`
- Set the `toolChain` property of a task to a locally installed tool via `toolChain.set(plainc.localTool("version", "/path/to/tool", ".o"))`
- Set the `toolChain` property of a task to a tool located in a ZIP file in a Maven repository via `toolChain.set(plainc.repositoryTool("group", "name", "version", "path/in/zip", ".o"))`

## Example

See: [sample/build.gradle.kts](sample/build.gradle.kts)

```
plugins {
    id("software.onepiece.plain-c")
}

tasks.register<CCompile>("compileC") {
    toolChain.set(plainc.localTool("14.0.0", "/usr/bin/clang", ".o"))
    targetPlatform.set(plainc.platform())

    source(layout.projectDirectory.files("src/c").asFileTree)
    includes(layout.projectDirectory.files("src/headers"))
    compilerArgs.add("-S")

    objectFileDir.set(layout.buildDirectory.dir("out/o"))
}

val compileC2 = tasks.register<CCompile>("compileC2") {
    toolChain.set(plainc.repositoryTool("com.example", "vendor-x", "1.2", "bin/xcc", ".src"))
    targetPlatform.set(plainc.platform())

    source(layout.projectDirectory.files("src/c-special").asFileTree)
    includes(layout.projectDirectory.files("src/headers"))

    objectFileDir.set(layout.buildDirectory.dir("out/src"))
}

tasks.register<Assemble>("assembleC2") {
    toolChain.set(plainc.repositoryTool("com.example", "vendor-x", "1.2", "bin/xas", ".o"))
    targetPlatform.set(plainc.platform())

    source(compileC2.map { it.objectFileDir.asFileTree })
    includes(layout.projectDirectory.files("src/headers"))
    assemblerArgs = listOf("-D")

    objectFileDir = layout.buildDirectory.dir("out/o2").get().asFile
}
```

## How does this work?

In order to fully configure one of the native tasks of Gradle (such as `org.gradle.language.c.tasks.CCompile`),
you require a **NativeToolchain** (instance of `org.gradle.nativeplatform.toolchain.NativeToolChain`)
and a **TargetPlatform** (instance of `org.gradle.nativeplatform.platform.NativePlatform`).
Gradle's own [native languages plugins](https://docs.gradle.org/current/userguide/plugin_reference.html#native_languages)
pre-configure these task properties through the so-called _native toolchain registry_.
This allows, for example, to compile the same code for multiple target platforms with different tools in the same build.
This is great, if this is what you need. However, in a scenario where we do not care about this feature and would rather register
tasks directly, we miss flexibility as the _native toolchain registry_ in Gradle is not very flexible right now and makes it difficult or impossible to register
your own tools (and it has not been improved for several years). 

The [PlainCExtension.java](src/main/java/software/onepiece/gradle/plainc/PlainCExtension.java) class now allows you to
set the `targetPlatform` and `toolChain` properties of any task without the _native toolchain registry_. 
It does so by utilising internal Gradle APIs to:
- Construct a generic `NativePlatform` object. This is the same everywhere and will be ignored during tool selection.
  In our scenario, we do not care about different platforms.
- Construct a `NativeToolChain` instance based on a concrete executable. Either by providing a concrete path to
  an executable (`localTool(...)`) or coordinates pointing at a ZIP file in a Maven repository (`repositoryTool(...)`). 
  In the latter case, Gradle's dependency management is used to find, download, extract and cache the tool. 

Behind the custom `NativeToolChain` implementation is a custom `PlatformToolProvider` implementation and extensions of 
Gradle's `NativeCompiler` implementations. This allows for more customization and extension of Gradle's functionality
in this area. This uses a lot of APIs from `internal` packages. Which, however, have not been touched for years. 

This showcase could help to determine which APIs in this area should probably be public.

## Disclaimer

⚠️ _This is work in progress_ ⚠️
