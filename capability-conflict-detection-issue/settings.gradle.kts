include("app", "extension")

dependencyResolutionManagement {
    repositories.mavenCentral()
    components {
        withModule("org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec") {
            val version = id.version
            allVariants {
                withCapabilities {
                    addCapability("javax.transaction", "javax.transaction-api", version)
                }
            }
        }
    }
}