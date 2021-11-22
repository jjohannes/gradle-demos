plugins {
    id("groovy-gradle-plugin")
}

dependencies {
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.7.1")
}

gradlePlugin {
    plugins.create("java-impl") {
        id = "my-smart-settings-plugin-java-impl"
        implementationClass = "MySmartSettingPlugin"
    }
}