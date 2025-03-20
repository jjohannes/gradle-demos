plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.app"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// we do not reuse the BuildTypeAttr.ATTRIBUTE to not confuse Android Studio about Java vs Android library
val flavorAttribute = Attribute.of("org.example.flavor", String::class.java)
configurations.matching { it.name.matches(Regex("release.+Classpath")) }.configureEach {
    attributes.attribute(flavorAttribute, "prod")
}
configurations.matching { it.name.matches(Regex("debug.+Classpath")) }.configureEach {
    attributes.attribute(flavorAttribute, "dev")
}

dependencies {
    implementation(project(":common"))
}