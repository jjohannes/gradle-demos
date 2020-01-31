plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js()
    macosX64()
    linuxX64()
}

publishing {
    publications.forEach { println("Koltin-Native publication: ${it.name}") }
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
    "jvmMainImplementation"(kotlin("stdlib"))
    "jsMainImplementation"(kotlin("stdlib-js"))
}