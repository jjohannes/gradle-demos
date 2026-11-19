package software.onepiece.playdate.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.process.ExecOperations
import javax.inject.Inject

@CacheableTask
abstract class PlaydateCompile : DefaultTask() {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val playdateSdk: DirectoryProperty

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val gameSource: DirectoryProperty

    @get:OutputDirectory
    abstract val pdx: DirectoryProperty

    @get:Inject
    abstract val execOperations : ExecOperations

    @TaskAction
    fun runInstall() {
        execOperations.exec {
            commandLine(playdateSdk.file("bin/pdc").get().asFile, gameSource.get().asFile, pdx.get().asFile)
        }
    }
}