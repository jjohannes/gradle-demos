plugins {
    id("application")
   // id("org.gradlex.jvm-dependency-conflict-resolution") version "2.1.2" // for patching POM metadata
   // id("org.gradlex.extra-java-module-info") version "1.10.1"            // for patching 'module-info.class'
}

application {
    mainModule = "org.example.log4j.metadata"
    mainClass = "org.example.App" // see: src/main/java/org/example/App.java
}

dependencies {
    // To make the example compile, build '2.25.0-SNAPSHOT' from the PR associated with
    // https://github.com/apache/logging-log4j2/issues/3437
    implementation("org.apache.logging.log4j:log4j-api:2.25.0-SNAPSHOT")
    // Or use release '2.24.3' and activate the patching rules below
    // implementation("org.apache.logging.log4j:log4j-api:2.24.3")
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Werror")
    options.compilerArgs.add("-Xlint:all") // includes 'classfile' check
}

/*

// To get the check passing, we first need the annotation libraries on the compile classpath.
// The user could define them directly as dependencies, but it would be more correct if Log4j
// has them as transitive dependencies – at COMPILE TIME ONLY (compileOnlyApiDependency scope).
jvmDependencyConflicts.patch {
    // Make annotation classes used by 'log4j' avaliable at compile time
    module("org.apache.logging.log4j:log4j-api") {
        addCompileOnlyApiDependency("com.google.errorprone:error_prone_annotations:2.36.0")
        addCompileOnlyApiDependency("com.github.spotbugs:spotbugs-annotations:4.9.0")
        addCompileOnlyApiDependency("biz.aQute.bnd:biz.aQute.bnd.annotation:7.1.0")
        addCompileOnlyApiDependency("org.osgi:org.osgi.annotation.bundle:2.0.0")
        addCompileOnlyApiDependency("org.jspecify:jspecify:1.0.0")
    }
}

// Things would work now, if we not use the Module Path for compilation. But if we do, classes
// of above libraries only become visible with the necessary 'require static' entries.
extraJavaModuleInfo {
    module("org.apache.logging.log4j:log4j-api", "org.apache.logging.log4j") {
        preserveExisting()
        requiresStatic("com.google.errorprone.annotations")
        requiresStatic("com.github.spotbugs.annotations")
        requiresStatic("biz.aQute.bnd.annotation")
        requiresStatic("org.osgi.annotation.bundle")
        // 'requires static org.jspecify' is already present
    }
    // fishy: transitive dependencies of annotation libraries without Module Name....
    failOnMissingModuleInfo = false
}

*/
