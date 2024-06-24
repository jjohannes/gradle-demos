package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

abstract class KMPAppPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        plugins.apply(KMPBasePlugin::class.java)

        val kotlin = extensions.getByType(KotlinMultiplatformExtension::class.java)
        kotlin.macosArm64().binaries.executable()
    }
}
