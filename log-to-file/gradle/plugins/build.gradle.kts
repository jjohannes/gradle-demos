plugins {
    `java-gradle-plugin`
}

gradlePlugin.plugins.create("org.example.log-to-file") {
    id = name
    implementationClass = "org.example.LogToFilePlugin"
}
