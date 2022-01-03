package de.jjohannes.codegen

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

interface CodeGeneratorConfig {
    @get:Input
    val projectName: Property<String>
    @get:Input
    val projectPackage: Property<String>
    @get:Input
    val initialCycleCount: Property<Int>
}
