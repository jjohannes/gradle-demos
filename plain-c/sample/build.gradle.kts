import software.onepiece.gradle.plainc.tasks.ExtendedCCompile
import software.onepiece.gradle.plainc.tasks.IcrementalAssemble

plugins {
    id("base")
    id("software.onepiece.plain-c")
}

tasks.register<ExtendedCCompile>("compileC") {
    group = "mytasks"

    source(layout.projectDirectory.files("src/c").asFileTree)
    includes(layout.projectDirectory.files("src/headers"))
    compilerArgs.add("-S")

    perFileCompilerArgs.put("test.c", listOf("-v"))

    toolChain.set(plainc.localTool("14.0.0", "/usr/bin/clang", ".o"))
    targetPlatform.set(plainc.platform())

    objectFileDir.set(layout.buildDirectory.dir("out/o"))
}

val compileC2 = tasks.register<ExtendedCCompile>("compileC2") {
    group = "mytasks"

    toolChain.set(plainc.repositoryTool("com.example", "vendor-x", "1.2", "bin/xcc", ".src"))
    targetPlatform.set(plainc.platform())

    source(layout.projectDirectory.files("src/c-special").asFileTree)
    includes(layout.projectDirectory.files("src/headers"))

    objectFileDir.set(layout.buildDirectory.dir("out/src"))
}

tasks.register<IcrementalAssemble>("assembleC2") {
    group = "mytasks"

    toolChain.set(plainc.repositoryTool("com.example", "vendor-x", "1.2", "bin/xas", ".o"))
    targetPlatform.set(plainc.platform())

    source(compileC2.map { it.objectFileDir.asFileTree })
    includes(layout.projectDirectory.files("src/headers"))
    assemblerArgs = listOf("-D")

    objectFileDir = layout.buildDirectory.dir("out/o").get().asFile
}

repositories.maven("../sample-repo") {
    metadataSources.artifact()
}
