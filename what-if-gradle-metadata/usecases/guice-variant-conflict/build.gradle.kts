plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
}

dependencies {
    // because both capabilities are requested, the 'main' variant is chosen (which provides 'no_aop' + more)
    implementation("com.google.inject:guice:4.2.2") // implicitly requires the 'com.google.inject:guice' capability
    implementation("com.google.inject:guice:4.2.2") {
        capabilities {
            requireCapability("com.google.inject:guice-no-aop")
        }
    }

    constraints {
        implementation("com.google.guava:guava:28.1-jre") // use Guava version that's in our repo with GMM
    }

    configurations.all {
        resolutionStrategy.capabilitiesResolution.withCapability("com.google.inject:guice-no-aop") {
            // select(candidates.first { !it.variantName.contains("noAop") })
        }
    }
}