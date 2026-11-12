package software.onepiece.gradle.plainc;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import software.onepiece.gradle.plainc.toolchain.transform.ExtractZipTransform;

import static org.gradle.api.artifacts.type.ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE;
import static software.onepiece.gradle.plainc.PlainCExtension.EXTRACTED_TOOLS_ATTRIBUTE;


@SuppressWarnings("unused")
abstract public class PlainCPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        // Register extension
        project.getExtensions().create("plainc", PlainCExtension.class);

        // Make the ExtractZipTransform known
        project.getDependencies().getAttributesSchema().attribute(EXTRACTED_TOOLS_ATTRIBUTE);
        project.getDependencies().getArtifactTypes().maybeCreate("zip").getAttributes().attribute(EXTRACTED_TOOLS_ATTRIBUTE, false);
        project.getDependencies().registerTransform(ExtractZipTransform.class, t -> {
            t.getFrom().attribute(EXTRACTED_TOOLS_ATTRIBUTE, false).attribute(ARTIFACT_TYPE_ATTRIBUTE, "zip");
            t.getTo().attribute(EXTRACTED_TOOLS_ATTRIBUTE, true).attribute(ARTIFACT_TYPE_ATTRIBUTE, "zip");
        });
    }
}
