plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

val gradleModuleMetadata: Configuration by configurations.creating {
    attributes {
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("gradle-module-metadata"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}
val allFiles: Configuration by configurations.creating {
    attributes {
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}
val allFilesWithDependencies: Configuration by configurations.creating {
    attributes {
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files-with-dependencies"))
    }
    extendsFrom(configurations.runtimeClasspath.get())
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.7")
    implementation("org.junit.jupiter:junit-jupiter-api:5.6.0-M1")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0-M1")

    components {
        all<DirectMetadataAccessVariantRule>()
    }
}



tasks.create("downloadMetadata") {
    doLast {
        // gradleModuleMetadata.incoming.artifacts.artifactFiles.forEach { println(it.readText()) }
        println("Only metadata:")
        gradleModuleMetadata.incoming.artifactView {
            lenient(true)
        }.files.forEach { println(it.name) }

        println("")
        println("All files:")
        allFiles.incoming.artifactView {
            lenient(true)
        }.files.forEach { println(it.name) }

        println("")
        println("All files with dependencies:")
        allFilesWithDependencies.incoming.artifactView {
            lenient(true)
        }.files.forEach { println(it.name) }

    }
}

@CacheableRule
open class DirectMetadataAccessVariantRule : ComponentMetadataRule {
    @javax.inject.Inject
    open fun getObjects(): ObjectFactory = throw UnsupportedOperationException()

    override fun execute(ctx: ComponentMetadataContext) {
        val id = ctx.details.id
        ctx.details.addVariant("moduleMetadata") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, getObjects().named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, getObjects().named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, getObjects().named("gradle-module-metadata"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.module")
            }
        }

        ctx.details.addVariant("allFiles") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, getObjects().named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, getObjects().named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, getObjects().named("all-files"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.module")
                addFile("${id.name}-${id.version}.jar")
            }
        }

        // TODO it would be better to add another variant instead of modifying "runtime".
        //      But at the moment we can only match by name here. And both can be there 'runtime' (pom) or 'runtimeElements' (gmm)
        ctx.details.withVariant("runtimeElements") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, getObjects().named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, getObjects().named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, getObjects().named("all-files-with-dependencies"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.module")
                addFile("${id.name}-${id.version}.jar")
            }
        }
        ctx.details.withVariant("runtime") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, getObjects().named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, getObjects().named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, getObjects().named("all-files-with-dependencies"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.jar")
            }
        }
    }

}