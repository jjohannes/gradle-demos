package org.example

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

abstract class FullTask : DefaultTask() {

    @get:InputFile
    abstract val decision: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun makeDecision() {
        if (decision.get().asFile.readText().isEmpty()) {
            throw StopExecutionException()
        }

        outputFile.get().asFile.writeText("full_generated!")
    }

}