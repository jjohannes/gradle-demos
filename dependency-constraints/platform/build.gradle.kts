plugins {
    id("java-platform")
}

dependencies.constraints {
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.okhttp3:okhttp:3.14.9")
    api("com.squareup.okio:okio:1.17.2")

    api("org.apache.logging.log4j:log4j-core") {
        version { reject("(,2.16.0)") }
        because("No matter what, we do not want these...")
    }
}
