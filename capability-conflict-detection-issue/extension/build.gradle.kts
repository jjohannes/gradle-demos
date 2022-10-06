plugins {
    id("java-library")
}

configurations.all {
    resolutionStrategy.capabilitiesResolution.withCapability("javax.transaction:javax.transaction-api") {
        select("javax.transaction:javax.transaction-api:0")
    }
}

dependencies {
    implementation("org.hibernate:hibernate-core:5.5.7.Final")
    implementation(project(":app"))
}