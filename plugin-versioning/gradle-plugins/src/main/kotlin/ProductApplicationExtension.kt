import org.gradle.api.provider.Property

interface ProductApplicationExtension {
    val frameworkVersion : Property<String>
}
