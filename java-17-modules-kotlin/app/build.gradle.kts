import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-17-module-kotlin-app")
}

application {
    mainClass.set("my.app.application.MyAppKt")
    mainModule.set("my.app.app")
}


kotlin {
    java
}
tasks.withType<KotlinCompile>() {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // corresponding module-info entry: 'requires my.app.moduletwo;'
    implementation(project(":moduletwo"))
    // corresponding module-info entry: 'io.github.classgraph;'
    implementation("io.github.classgraph:classgraph")
}
