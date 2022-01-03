package de.jjohannes.codegen.gradle

import de.jjohannes.codegen.CodeGenerator
import de.jjohannes.codegen.CodeGeneratorConfig

object SettingsGradleGen : CodeGenerator {

    override fun fileName(conf: CodeGeneratorConfig) = with(conf) {
        "settings.gradle.kts"
    }

    override fun generateContent(conf: CodeGeneratorConfig) = with(conf) {
        """
            dependencyResolutionManagement {
                repositories.mavenCentral()
            }
        """
    }
}