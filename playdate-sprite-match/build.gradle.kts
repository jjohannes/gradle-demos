plugins {
    id("software.onepiece.playdate-picture-game") version "1.0"
}

dependencies {
    // You can change the version of the SDK dependency
    playdateSdk("playdate:PlaydateSDK:1.12.3")

    // You can change the dependency to another asset pack
    kenneyGameAssets("58-bit-pack:1bitpack_kenney_1.2:1")
    // kenneyGameAssets("4-bit-input-prompts-pixel-16:1-bit-input-prompts-pixel-16:1")
    // kenneyGameAssets("39-bit-platformer-pack:1bitplatformerpack:1")
}



// If you want to see parallel task execution - enabled by 'org.gradle.unsafe.configuration-cache=true' - in action,
// you can add some delay to these tasks. You have to run the build twice:
// - ./gradlew :compilePlaydate --dry-run    <- due to https://github.com/orgs/gradle/projects/32/views/1
// - ./gradlew :compilePlaydate              <- You can see the tasks running in parallel

// tasks.installPlaydateSdk {
//     doLast { Thread.sleep(5000) }
// }
// tasks.processGameAssetPacks {
//     doLast { Thread.sleep(5000) }
// }
