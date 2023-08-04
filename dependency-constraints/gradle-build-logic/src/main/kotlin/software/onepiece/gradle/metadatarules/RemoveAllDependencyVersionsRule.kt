package software.onepiece.gradle.metadatarules

import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule

@CacheableRule
abstract class RemoveAllDependencyVersionsRule : ComponentMetadataRule {
    override fun execute(context: ComponentMetadataContext) {
        val myId = context.details.id
        context.details.allVariants {
            withDependencies {
                forEach {
                    it.version {
                        // If the other component does not align with myself (same group and version)
                        // don't trust whatever version was published.
                        if (myId.group != it.group && myId.version != requiredVersion) {
                            require("")
                        }
                    }
                }
            }
            // If there are explicit constraints, remove them all.
            withDependencyConstraints { clear() }
        }
    }
}