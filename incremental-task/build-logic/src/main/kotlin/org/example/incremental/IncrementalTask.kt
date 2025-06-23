package org.example.incremental

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges

@CacheableTask
abstract class IncrementalTask : DefaultTask() {

    @get:InputFiles
    @get:Incremental
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val sourceFiles: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outDir: DirectoryProperty

    @TaskAction
    fun generate(inputs: InputChanges) {
        if (inputs.isIncremental) {
            println("CHANGED: " + inputs.getFileChanges(sourceFiles))
            inputs.getFileChanges(sourceFiles).forEach {
                val outFile = outDir.file(it.file.name + ".txt").get().asFile
                if (it.changeType == ChangeType.REMOVED) {
                    outFile.delete()
                } else {
                    outFile.writeText("out - " + it.file.readText())
                }
            }
        } else {
            println("FULL")
            sourceFiles.asFileTree.files.forEach {
                outDir.file(it.name + ".txt").get().asFile.writeText("out - " + it.readText())
            }
        }
    }

}