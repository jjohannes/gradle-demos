plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "my.app"

// We assume that the module name (defined in module-info.java) corresponds to a combination of group and project name
val moduleName = "${project.group}.${project.name}"
val testModuleName = "${moduleName}.test"

// Use Java 17
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

// this is needed because we have a separate compile step in this example with the 'module-info.java' is in 'main/java' and the Kotlin code is in 'main/kotlin'
tasks.compileJava {
    // Compiling module-info in the 'main/java' folder needs to see already compiled Kotlin code
    options.compilerArgs = listOf("--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}")
}

// Testing with JUnit5 (which is available in modules)
tasks.compileTestKotlin {
    // Make sure only module Jars are on the classpath and not the classes folders of the current project
    // libraries.from(configurations.testCompileClasspath)
}
tasks.compileTestJava {
    // Compiling module-info in the 'test/java' folder needs to see already compiled Kotlin code
    options.compilerArgs = listOf("--patch-module", "$testModuleName=${sourceSets.test.get().output.asPath}")
    // Make sure only module Jars are on the classpath and not the classes folders of the current project
    classpath = configurations.testCompileClasspath.get()
}
val testJar = tasks.register<Jar>(sourceSets.test.get().jarTaskName) {
    // Package test code/resources as Jar so that they are a proper module at runtime
    archiveClassifier.set("tests")
    from(sourceSets.test.get().output)
}
tasks.test {
    classpath = configurations.testRuntimeClasspath.get() + files(testJar)
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    // corresponding test module-info entry: 'requires <name-of-main-module>;'
    testImplementation(project) // depend on 'main' module in this project
}

// Versions for published modules
dependencies.constraints {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0") {
        because("Module: com.fasterxml.jackson.databind")
    }
    implementation("io.github.classgraph:classgraph:4.8.117") {
        because("Module: io.github.classgraph")
    }
}
