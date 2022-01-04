plugins {
    id("java-library")
    id("de.jjohannes.missing-metadata-guava") version "0.5"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:28.0-jre")
    implementation("org.codehaus.plexus:plexus-container-default:2.1.0")
    implementation("com.google.api-client:google-api-client:1.30.7")
}

tasks.register("checkClasspath") {
    doLast {
        val compile = configurations.compileClasspath.get().map { it.name }
        val rt = configurations.runtimeClasspath.get().map { it.name }
        println("=== JARs on compile classpath ===")
        compile.forEach { println("- $it") }
        println()
        println("=== JARs on runtime classpath ===")
        rt.forEach { println("- $it") }
    }
}
