package de.jjohannes.codegen.xml

import de.jjohannes.codegen.CodeGenerator
import de.jjohannes.codegen.CodeGeneratorConfig

object ConfigXmlGen : CodeGenerator {

    override fun fileName(conf: CodeGeneratorConfig) = with(conf) {
        "src/main/resources/conf.xml"
    }

    override fun generateContent(conf: CodeGeneratorConfig) = with(conf) {
        """
            <conf>
                <cycles count="${initialCycleCount.get()}"/>
            </conf>
        """
    }
}