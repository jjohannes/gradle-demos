plugins {
    kotlin("multiplatform")
}

kotlin {
    macosX64 {
        binaries {
            executable()
        }
    }
}

configurations["macosX64CompileKlibraries"].apply {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    }
}

dependencies {
    "commonMainImplementation"(kotlin("stdlib-common"))
    "commonMainImplementation"("example:kotlin-multiplatform-library:1.0")
    //"commonMainImplementation"("example:kotlin-library:1.0")
}