package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class KMPLibPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        plugins.apply(KMPBasePlugin::class.java)
    }
}