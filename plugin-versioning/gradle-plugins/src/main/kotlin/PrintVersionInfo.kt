import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class PrintVersionInfo : DefaultTask() {

    @get:Input
    abstract val frameworkVersion : Property<String>

    @TaskAction
    fun print() {
        println("JAVA:      ${Versions.ACTUAL_JAVA}")
        println("GRADLE:    ${Versions.ACTUAL_GRADLE}")
        println("PLUGINS:   ${Versions.ACTUAL_PLUGINS}")
        println("FRAMEWORK: ${frameworkVersion.get()}")
    }
}