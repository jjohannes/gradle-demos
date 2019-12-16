subprojects {
    plugins.withType<GroovyPlugin> {
        extensions.getByType<JavaPluginExtension>().apply {
            // activate sources variant (includes sources jar task and 'sourcesElements' configuration)
            withSourcesJar()
        }
        val sourcesElements: Configuration by configurations.getting {
            // include sources of dependencies
            extendsFrom(configurations["implementation"])
            outgoing.variants.create("sourcesDirectory") {
                attributes.attribute(Attribute.of("artifactType", String::class.java), "java-sources-directory")
                val sourceSet = the<SourceSetContainer>()[SourceSet.MAIN_SOURCE_SET_NAME]
                sourceSet.java.srcDirs.forEach {
                    artifact(it)
                }
                sourceSet.groovy.srcDirs.forEach {
                    artifact(it)
                }
            }
        }

        // create a 'sourcesPath' configuration to resolve sources
        val sourcesPath: Configuration by configurations.creating {
            isCanBeConsumed = false
            extendsFrom(configurations["implementation"])
            attributes {
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
            }
        }
    }
}

val SourceSet.groovy: SourceDirectorySet
    get() = withConvention(GroovySourceSet::class) { groovy }
