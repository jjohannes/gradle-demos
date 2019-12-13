subprojects {
    plugins.withType<JavaPlugin> {
        extensions.getByType<JavaPluginExtension>().apply {
            // activate sources variant (includes sources jar task and 'sourcesElements' configuration)
            withSourcesJar()
        }
        val sourcesElements: Configuration by configurations.getting {
            // include sources of dependencies
            extendsFrom(configurations["implementation"])
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
