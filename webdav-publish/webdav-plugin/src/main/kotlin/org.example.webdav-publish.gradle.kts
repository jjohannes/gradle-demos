import org.example.webdav.PublishWebDav
import org.example.webdav.PublishWebDavExtension

plugins {
    id("java-library")
    id("maven-publish")
}

val publishingWebDav = extensions.create<PublishWebDavExtension>("publishingWebDav")
val localStagingRepo = layout.buildDirectory.dir("local-staging-repo")

publishing {
    repositories.maven {
        name = "localStaging"
        url = uri(localStagingRepo)
    }
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

val publishWebdav = tasks.register<PublishWebDav>("publishWebdav") {
    localRepo.set(tasks.named("publishMavenPublicationToLocalStagingRepository").map { localStagingRepo.get() })
    remote.convention(publishingWebDav.repositoryUrl)
    username.convention(publishingWebDav.username)
    password.convention(publishingWebDav.password)
}

tasks.publish {
    dependsOn(publishWebdav)
}
