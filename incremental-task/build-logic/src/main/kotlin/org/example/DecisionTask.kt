package org.example

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.util.Date

abstract class DecisionTask : DefaultTask() {

    @get:InputFile
    abstract val changedFile: RegularFileProperty

    @get:OutputFile
    abstract val fullBuildDecision: RegularFileProperty

    @get:OutputFile
    abstract val deltaBuildDecision: RegularFileProperty

    @TaskAction
    fun makeDecision() {
        val full = changedFile.get().asFile.readText().isBlank()

        if (full) {
            fullBuildDecision.get().asFile.writeText(Date().toString())
        } else {
            deltaBuildDecision.get().asFile.writeText(Date().toString())
        }

        if (!deltaBuildDecision.get().asFile.exists()) {
            fullBuildDecision.get().asFile.writeText("")
        }
        if (!fullBuildDecision.get().asFile.exists()) {
            fullBuildDecision.get().asFile.writeText("")
        }
    }

}