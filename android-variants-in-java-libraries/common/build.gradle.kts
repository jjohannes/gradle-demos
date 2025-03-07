import com.android.build.api.attributes.BuildTypeAttr

plugins {
    id("java-library")
}

java.registerFeature("debug") {
   usingSourceSet(sourceSets.main.get())
    // set to default capability
    capability(project.group.toString(), project.name, project.version.toString())
}

configurations.apiElements {
    attributes.attribute(BuildTypeAttr.ATTRIBUTE, objects.named("release"))
}
configurations.runtimeElements {
    attributes.attribute(BuildTypeAttr.ATTRIBUTE, objects.named("release"))
}
configurations.getByName("debugApiElements") {
    attributes.attribute(BuildTypeAttr.ATTRIBUTE, objects.named("debug"))
}
configurations.getByName("debugRuntimeElements") {
    attributes.attribute(BuildTypeAttr.ATTRIBUTE, objects.named("debug"))
}


dependencies {
    api("org.bouncycastle:bcprov-jdk18on:1.79") // at prod time
    "debugApi"("org.bouncycastle:bcprov-debug-jdk18on:1.79") // at dev time
}
