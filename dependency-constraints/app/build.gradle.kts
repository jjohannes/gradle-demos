plugins {
    id("my-java-application")
}

dependencies {
    implementation(platform(project(":platform")))

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
}

application {
    mainClass.set("software.onepiece.someapp.App")
}