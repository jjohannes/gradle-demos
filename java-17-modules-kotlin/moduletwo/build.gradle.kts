plugins {
    id("java-17-module-kotlin-lib")
}

dependencies {
    // corresponding module-info entry: 'requires com.fasterxml.jackson.databind;'
    implementation("com.fasterxml.jackson.core:jackson-databind")
    // corresponding module-info entry: 'requires transitive my.app.moduleone;'
    api(project(":moduleone"))
}
