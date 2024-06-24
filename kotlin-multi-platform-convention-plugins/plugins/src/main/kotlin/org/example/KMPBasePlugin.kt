package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

abstract class KMPBasePlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        plugins.apply(KotlinMultiplatformPluginWrapper::class.java)

        // Configure Kotlin MP targets
        val kotlin = extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.jvm()
        kotlin.js{ browser() }
        kotlin.macosArm64()
    }
}
