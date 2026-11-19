plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
}

dependencies {
    implementation("com.google.inject:guice:4.2.2") {
        capabilities {
            requireCapability("com.google.inject:guice-no-aop")
        }
    }

    constraints {
        implementation("com.google.guava:guava:28.1-jre") // use Guava version that's in our repo with GMM
    }
}
