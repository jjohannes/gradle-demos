package org.gradle.kotlin.dsl

import custom.buildsystem.MyDslExtension
import org.gradle.api.Project

val Project.mydsl
    get() = extensions.getByType(MyDslExtension::class.java)

fun Project.mydsl(action: MyDslExtension.() -> Unit) =
    extensions.getByType(MyDslExtension::class.java).apply(action)
