import org.example.gradle.metadatarules.DirectMetadataAccessVariantRule

plugins {
    id("java-library")
}

val gradleModuleMetadata = configurations.create("gradleModuleMetadata") {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION) )
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("gradle-module-metadata"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}
val allFiles = configurations.create("allFiles") {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION) )
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}
val allFilesWithDependencies = configurations.create("allFilesWithDependencies") {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION) )
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files-with-dependencies"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

dependencies.components {
    all<DirectMetadataAccessVariantRule>()
}

tasks.register("downloadMetadata") {
    doLast {
        // gradleModuleMetadata.incoming.artifacts.artifactFiles.forEach { println(it.readText()) }
        println("Only metadata:")
        gradleModuleMetadata.incoming.artifactView {
            lenient(true) // sometimes '.module' is missing
        }.files.forEach { println(it.name) }

        println("")
        println("All files:")
        allFiles.incoming.artifactView {
            lenient(true) // sometimes '.module' is missing
        }.files.forEach { println(it.name) }

        println("")
        println("All files with dependencies:")
        allFilesWithDependencies.incoming.files.forEach { println(it.name) }
    }
}
