plugins {
    id("java-library")
}

java.registerFeature("dev") {
    // register additional source set as workaround for deprecation:
    // https://docs.gradle.org/8.13/userguide/upgrading_version_8.html#deprecate_register_feature_main_source_set
    usingSourceSet(sourceSets.create("dev"))
    // set to default capability
    capability(project.group.toString(), project.name, project.version.toString())
}

// we do not reuse the BuildTypeAttr.ATTRIBUTE to not confuse Android Studio about Java vs Android library
val flavorAttribute = Attribute.of("org.example.flavor", String::class.java)
configurations.matching { it.name in listOf("apiElements", "runtimeElements") }.configureEach {
    attributes.attribute(flavorAttribute, "prod")
}
configurations.matching { it.name in listOf("devApiElements", "devRuntimeElements") }.configureEach {
    attributes.attribute(flavorAttribute, "dev")
    // use compiled code from "main" source set instead of (non-existing) "dev" source set
    outgoing.artifacts.clear()
    outgoing.artifact(tasks.jar)
    outgoing.variants.named("classes") {
        artifacts.clear()
        artifact(sourceSets.main.get().java.classesDirectory)
    }
}

dependencies {
    api("org.bouncycastle:bcprov-jdk18on:1.79") // at prod time
    "devApi"("org.bouncycastle:bcprov-debug-jdk18on:1.79") // at dev time
}
