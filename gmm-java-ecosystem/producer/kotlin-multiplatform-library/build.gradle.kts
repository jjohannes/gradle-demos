plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }
    macosX64()
    linuxX64()
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
    "jvmMainImplementation"(kotlin("stdlib"))
    "jsMainImplementation"(kotlin("stdlib-js"))
}
