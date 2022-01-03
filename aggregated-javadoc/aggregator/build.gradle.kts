plugins {
    id("my-javadoc")
}

dependencies {
    rootProject.subprojects.forEach { subproject ->
        if (subproject.name != "aggregator") {
            implementation(subproject)
        }
    }
}
