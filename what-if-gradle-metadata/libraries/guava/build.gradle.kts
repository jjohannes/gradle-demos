import groovy.util.Node
import java.net.URL

plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "com.google.guava"
version = "28.1-jre"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// === COMPILE ONLY DEPENDENCIES FOR THE API ===
// Solves https://github.com/google/guava/issues/2824#issuecomment-303425035
// It's totally fine to do the following. And 'compileOnlyApi' could be something the java-library plugin defines out-of-the-box in the future.
val compileOnlyApi: Configuration by configurations.creating
configurations["compileClasspath"].extendsFrom(compileOnlyApi)
configurations["apiElements"].extendsFrom(compileOnlyApi)

dependencies {
    api("com.google.guava:failureaccess:1.0.1")
    // Annotations that may be required at compile and runtime
    api("org.checkerframework:checker-qual")
    // the following annotations are no longer required at runtime
    compileOnlyApi("com.google.code.findbugs:jsr305")
    compileOnlyApi("com.google.errorprone:error_prone_annotations")
    compileOnlyApi("com.google.j2objc:j2objc-annotations")

    api(platform("com.google.guava:guava-parent:28.1-jre"))
}

// === DETECTING CONFLICT WITH com.google.collections ===
// By stating that Guava provides the 'com.google.collections' capability, Gradle will detect the conflict if both are in the dependnecy graph
configurations {
    api {
        outgoing {
            capability("com.google.collections:google-collections:${project.version}")
        }
    }
}

// === DETECTING CONFLICT WITH EXTERNAL listenablefuture LIBRARY ===
// Better solves https://github.com/google/guava/issues/3320 (and related)
// Using capabilities, we express that 'guava' itself already contains 'listenablefuture' itself (this may need to go on 'failureaccess' instead)
// Gradle will fail if two dependencies provide the same capability and offers strategy to resolve the conflict
configurations {
    api {
        outgoing {
            capability("com.google.guava:${project.name}:${project.version}")
            capability("com.google.guava:listenablefuture:1.0")
        }
    }
}


publishing {
    publications {
        create<MavenPublication>("guava") {
            from(components["java"])

            // we add the '9999.0-empty-to-avoid-conflict-with-guava' fix back here for Maven consumers
            // We accept the 'capability com.google.guava:listenablefuture:1.0 which cannot be mapped to Maven' warning,
            // because with this workaround we have a solution for Maven
            pom.withXml {
                val dependencies = asNode().children().first { it is Node && it.name().toString().endsWith("dependencies") } as Node
                val listenablefutureWorkaroundDependency = dependencies.appendNode("dependency")
                listenablefutureWorkaroundDependency.appendNode("groupId", "com.google.guava")
                listenablefutureWorkaroundDependency.appendNode("artifactId", "listenablefuture")
                listenablefutureWorkaroundDependency.appendNode("version", "9999.0-empty-to-avoid-conflict-with-guava")
            }
            suppressAllPomMetadataWarnings()
        }
    }
}

tasks.withType<Jar> {
    actions = listOf()
    doLast {
        val path = project.group.toString().replace('.', '/') + '/' + project.name + '/' + project.version + '/' + archiveFileName.get()
        archiveFile.get().asFile.writeBytes(URL("https://repo1.maven.org/maven2/$path").readBytes())
    }
}

// === PROVIDING JDK6 AND JDK8 VARIANTS ===
// Gradle allows you to publish multiple variants of one compoent
// The consumer can then choose one variants
// In this case, everything is already set up on the consumer side so that Gradle consumers will
// choose the right variant based on their `targetCompatibility` setting.

// Emulating the creation of th JDK6 Android Jar
val jarJdk6 = tasks.create<Jar>("jarJdk6") {
    archiveClassifier.value("android")
    actions = listOf()
    doLast {
        val path = "com/google/guava/guava/28.1-android/guava-28.1-android.jar"
        archiveFile.get().asFile.writeBytes(URL("https://repo1.maven.org/maven2/$path").readBytes())
    }
}

// Defining the additional JDK6 variants
val jdk6ApiElements: Configuration by configurations.creating {
    isCanBeConsumed = true; isCanBeResolved = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 6)
    }
    outgoing.artifact(jarJdk6)
    extendsFrom(configurations["api"])
    extendsFrom(compileOnlyApi)
}
val jdk6RuntimeElements: Configuration by configurations.creating {
    isCanBeConsumed = true; isCanBeResolved = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(LibraryElements.JAR))
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 6)
    }
    outgoing.artifact(jarJdk6)
    extendsFrom(configurations["implementation"])
    extendsFrom(configurations["runtimeOnly"])
}
val javaComponent = components.findByName("java") as AdhocComponentWithVariants
javaComponent.addVariantsFromConfiguration(jdk6ApiElements) {}
javaComponent.addVariantsFromConfiguration(jdk6RuntimeElements) {}

