import software.onepiece.playdate.gradle.tasks.GameAssetPacksProcessor
import software.onepiece.playdate.gradle.tasks.PlaydateCompile
import software.onepiece.playdate.gradle.tasks.PlaydateSdkInstall

val playdateSdk by configurations.creating
val kenneyGameAssets by configurations.creating

repositories {
    ivy {
        name = "PlaydateSdks"
        url = uri("https://download-keycdn.panic.com/playdate_sdk")
        patternLayout {
            artifact("[module]-[revision].zip")
        }
        metadataSources {
            artifact()
        }
        content {
            onlyForConfigurations(playdateSdk.name)
        }
    }
    ivy {
        name = "KennyGameAssets"
        url = uri("https://www.kenney.nl/content/3-assets")
        patternLayout {
            artifact("[organization]/[module].zip")
        }
        metadataSources {
            artifact()
        }
        content {
            onlyForConfigurations(kenneyGameAssets.name)
        }
    }

}

val installPlaydateSdk = tasks.register<PlaydateSdkInstall>("installPlaydateSdk") {
    sdkPkg.from(zipTree(playdateSdk.elements.map { it.first() }).matching { exclude("__MACOSX") })
    sdkLocation.set(providers.systemProperty("user.home").map { File(File(it), "Developer/PlaydateSDK") }.get())
}

val processGameAssetPacks = tasks.register<GameAssetPacksProcessor>("processGameAssetPacks") {
    assetPack.from(zipTree(kenneyGameAssets.elements.map { it.single() }).matching {
        include("**/monochrome.png")
        include("**/monochrome_tilemap.png")
        include("**/tilemap_black.png")
    })
    spriteSize.set(16)
    spriteSpacing.set(1)
    assetFolder.set(layout.buildDirectory.dir("assets"))
}

val mergeSourceAndAssets = tasks.register<Sync>("mergeSourceAndAssets") {
    destinationDir = layout.buildDirectory.dir("merged").get().asFile

    from(layout.projectDirectory.dir("source"))
    from(processGameAssetPacks) { into("images") }
}

tasks.register<PlaydateCompile>("compilePlaydate") {
    playdateSdk.set(installPlaydateSdk.flatMap { it.sdkLocation })
    gameSource.set(layout.dir(mergeSourceAndAssets.map { it.destinationDir }))
    pdx.set(layout.buildDirectory.dir("dist/${project.name}.pdx"))
}
