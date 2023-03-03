plugins {
    id("java-gradle-plugin")
}

gradlePlugin.plugins.create("dummy") {
    id = name
    implementationClass = "org.example.plugin.DummyPlugin"
}
