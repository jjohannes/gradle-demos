package com.example.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

@CacheableTask
abstract class ReadmeConvert : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val fromMd: RegularFileProperty;

    @get:OutputFile
    abstract val toTxt: RegularFileProperty;

    @TaskAction
    fun convert() {
        toTxt.get().asFile.writeText("readme.txt: " + fromMd.get().asFile.readText())
    }
}