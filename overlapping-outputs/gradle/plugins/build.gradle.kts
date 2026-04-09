plugins {
    id("java-gradle-plugin")
}

gradlePlugin.plugins.register("code-gen") {
    id = name
    implementationClass = "CodeGenPlugin"
}
