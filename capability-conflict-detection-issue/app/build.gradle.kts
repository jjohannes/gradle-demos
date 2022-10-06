plugins {
    id("java-library")
}

dependencies {
    // Change the order of these two dependencies to have the capability conflict detected
    runtimeOnly("org.liquibase.ext:liquibase-hibernate5:4.4.3")
    runtimeOnly("org.eclipse.jetty.aggregate:jetty-all:9.4.35.v20201120")
}
