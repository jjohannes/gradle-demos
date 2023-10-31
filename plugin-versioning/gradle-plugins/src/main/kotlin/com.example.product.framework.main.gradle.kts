plugins {
    id("com.example.product.framework")
}

Versions.check(Versions.ACTUAL_PLUGINS)

configurations.runtimeElements {
    attributes.attribute(Versions.PLUGINS_VERSION_ATTRIBUTE, Versions.ACTUAL_PLUGINS)
}

tasks.register<PrintVersionInfo>("versionInfo") {
    group = HelpTasksPlugin.HELP_GROUP
    frameworkVersion = project.version.toString()
}