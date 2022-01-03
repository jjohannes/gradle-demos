package de.jjohannes.codegen

interface CodeGenerator {

    fun fileName(conf: CodeGeneratorConfig): String

    fun generateContent(conf: CodeGeneratorConfig): String
}