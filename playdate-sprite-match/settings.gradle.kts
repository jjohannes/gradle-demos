pluginManagement {
    includeBuild("gradle/playdate-picture-game")
    // Enable if you have a Maven repository you published the plugin (then you can remove 'gradle/playdate-picture-game' and the includeBuild above)
    // See 'gradle/playdate-picture-game/build.gradle.kts' for publishing to the repository
    // repositories.maven("https://example.com/repo/releases")
}

buildCache {
    remote<HttpBuildCache> {
        isEnabled = false // change if you have a remote cache to point to below
        url = uri("https://example.com/gradle/cache/")
        isPush = true
        credentials {
            username = "jendrik"
            password = providers.environmentVariable("SUPER_SECRET").get()
        }
    }
}