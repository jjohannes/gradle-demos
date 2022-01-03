package de.jjohannes.codegen

import de.jjohannes.codegen.gradle.BuildGradleGen
import de.jjohannes.codegen.gradle.SettingsGradleGen
import de.jjohannes.codegen.java.AppJavaGen
import de.jjohannes.codegen.xml.ConfigXmlGen
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateCodeTask: DefaultTask() {

    private val generators = listOf(AppJavaGen, ConfigXmlGen, SettingsGradleGen, BuildGradleGen)

    @get:Nested
    abstract val conf: CodeGeneratorConfig

    @get:OutputDirectory
    abstract val targetDir: DirectoryProperty

    @TaskAction
    fun generate() {
        generators.forEach { generator ->
            with(File(targetDir.get().asFile, generator.fileName(conf))) {
                parentFile.mkdirs()
                writeText(generator.generateContent(conf).trimIndent())
            }
        }
    }
}