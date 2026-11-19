import java.net.URL

plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "org.lwjgl"
version = "3.2.3"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}


data class NativeVariant(val os: String, val arch: String, val classifier: String)
private val nativeVariants = listOf(
        NativeVariant(OperatingSystemFamily.LINUX,   "arm32",  "natives-linux-arm32"),
        NativeVariant(OperatingSystemFamily.LINUX,   "arm64",  "natives-linux-arm64"),
        NativeVariant(OperatingSystemFamily.WINDOWS, "x86",    "natives-windows-x86"),
        NativeVariant(OperatingSystemFamily.WINDOWS, "x86-64", "natives-windows"),
        NativeVariant(OperatingSystemFamily.MACOS,   "x86-64", "natives-macos")
)

// Add a different runtime variant for each platform
val javaComponent = components.findByName("java") as AdhocComponentWithVariants
nativeVariants.forEach { variantDefinition ->
    // Emulating the creation of the native jars
    val nativeJar = tasks.create<Jar>(variantDefinition.classifier + "Jar") {
        archiveClassifier.value(variantDefinition.classifier)
        actions = listOf()
        doLast {
            val path = "org/lwjgl/lwjgl/${project.version}/lwjgl-${project.version}-${variantDefinition.classifier}.jar"
            archiveFile.get().asFile.writeBytes(URL("https://repo1.maven.org/maven2/$path").readBytes())
        }
    }

    val nativeRuntimeElements = configurations.create(variantDefinition.classifier + "RuntimeElements") {
        isCanBeConsumed = true; isCanBeResolved = false
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
            attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
            attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
            attributes.attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, objects.named(variantDefinition.os))
            attributes.attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, objects.named(variantDefinition.arch))
        }
        outgoing.artifact(tasks.named("jar"))
        outgoing.artifact(nativeJar)
        extendsFrom(configurations["runtimeElements"])
    }
    javaComponent.addVariantsFromConfiguration(nativeRuntimeElements) {}
}

// don't publish the default runtime without native jar
javaComponent.withVariantsFromConfiguration(configurations["runtimeElements"]) {
    skip()
}

publishing {
    publications {
        create<MavenPublication>("slf4j") {
            from(components["java"])
        }
    }
}
