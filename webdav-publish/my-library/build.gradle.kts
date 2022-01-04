plugins {
    id("java-library")
    id("org.example.webdav-publish")
}

version = "1.0"
group = "org.test"

dependencies {
    implementation("com.google.guava:guava:26.0-jre")
}

publishingWebDav {
    repositoryUrl.set("dav:https://thewebdavurl")
    username.set("test")
    password.set("test")
}
