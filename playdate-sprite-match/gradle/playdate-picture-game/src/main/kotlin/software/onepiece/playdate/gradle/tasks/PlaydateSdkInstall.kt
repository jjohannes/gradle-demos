package software.onepiece.playdate.gradle.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class PlaydateSdkInstall : DefaultTask() {

    @get:InputFiles
    abstract val sdkPkg: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val sdkLocation: DirectoryProperty

    @get:Inject
    abstract val execOperations : ExecOperations

    @TaskAction
    fun runInstall() {
        execOperations.exec {
            commandLine("sudo", "installer", "-pkg", sdkPkg.singleFile, "-target", "/")
        }
    }
}