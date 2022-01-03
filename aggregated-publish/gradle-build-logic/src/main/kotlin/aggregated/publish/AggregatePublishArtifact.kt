package aggregated.publish

import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.tasks.TaskDependency
import java.io.File
import java.util.*

class AggregatePublishArtifact(private val name: String, private val resolver: Configuration): PublishArtifact {
    override fun getBuildDependencies(): TaskDependency = resolver.getTaskDependencyFromProjectDependency(true, "jar")

    override fun getName() = name

    override fun getExtension() = "jar"

    override fun getType() = "jar"

    override fun getClassifier(): String? = null

    override fun getFile(): File = resolver.files.first()

    override fun getDate(): Date? = null
}