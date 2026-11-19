import java.net.URL

plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "com.google.inject"
version = "4.2.2"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    // declare 'noAop' as another feature built from the same sources as the main feature
    java {
        registerFeature("noAop") {
            usingSourceSet(sourceSets["main"])
        }
    }
}

// "build" the no_aop jar
val jarNoAop = tasks.create<Jar>("jarNoAop") {
    archiveClassifier.value("no_aop")
    actions = listOf()
    doLast {
        val path = "com/google/inject/guice/${project.version}/guice-${project.version}-no_aop.jar"
        archiveFile.get().asFile.writeBytes(URL("https://repo1.maven.org/maven2/$path").readBytes())
    }
}

// == Setup specific feature relationship ==
val noAopApiElements: Configuration by configurations.getting
val noAopRuntimeElements: Configuration by configurations.getting
val apiElements: Configuration by configurations.getting
val runtimeElements: Configuration by configurations.getting
val noAopApi: Configuration by configurations.getting
val api: Configuration by configurations.getting
// 'no_aop' does not add an optional feature to the 'main' feature (the usual assumption), but is a reduced alternative.
//Hence we remove the 'main' jar (the default when basing a feature on the 'main' source set) and replace it with the 'no_aop' jar
noAopApiElements.outgoing {
    artifacts.clear()
    artifact(jarNoAop)
}
noAopRuntimeElements.outgoing {
    artifacts.clear()
    artifact(jarNoAop)
}

// All dependencies of 'no_aop' are also dependencies of the main variant which can both be expressed by 'extendsFrom'
api.extendsFrom(noAopApi)
apiElements.outgoing {
    // The main variant provides the 'default' capability (needs to be defined explicitly if other capabilities are assigned)...
    capability("$group:${project.name}:$version")
    // ...and the 'no-aop' capability. This will make Gradle choose the main variant (the one with 'everything') in case of conflict
    capability(noAopApiElements.outgoing.capabilities.first())
}
runtimeElements.outgoing {
    capability("$group:${project.name}:$version")
    capability(noAopRuntimeElements.outgoing.capabilities.first())
}

dependencies {
    noAopApi(platform("com.google.inject:guice-parent:$version"))

    noAopApi("javax.inject:javax.inject")
    noAopApi("com.google.guava:guava")

    api("aopalliance:aopalliance") // this is now a dependency of the main variant only
}

publishing {
    publications {
        create<MavenPublication>("jackson") {
            from(components["java"])
        }
    }
}
