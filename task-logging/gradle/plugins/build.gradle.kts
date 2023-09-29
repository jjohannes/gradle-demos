plugins {
    id("java-gradle-plugin")
}

gradlePlugin.plugins.create("my-build-system") {
    id = name
    implementationClass = "org.example.MyBuildSystemPlugin"
}