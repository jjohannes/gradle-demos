package org.example.gradle.metadatarules

import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.DocsType
import org.gradle.api.attributes.Usage
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import javax.inject.Inject

@CacheableRule
abstract class DirectMetadataAccessVariantRule : ComponentMetadataRule {

    @get:Inject
    abstract val objects: ObjectFactory

    override fun execute(ctx: ComponentMetadataContext) {
        val id = ctx.details.id
        ctx.details.addVariant("moduleMetadata") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("gradle-module-metadata"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.module")
            }
        }

        ctx.details.addVariant("allFiles") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.module")
                addFile("${id.name}-${id.version}.jar")
            }
        }

        ctx.details.maybeAddVariant("allFilesWithDependenciesElements", "runtimeElements") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files-with-dependencies"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.module")
                addFile("${id.name}-${id.version}.jar")
            }
        }
        ctx.details.maybeAddVariant("allFilesWithDependencies", "runtime") {
            attributes {
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
                attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("all-files-with-dependencies"))
            }
            withFiles {
                addFile("${id.name}-${id.version}.pom")
                addFile("${id.name}-${id.version}.jar")
            }
        }
    }

}