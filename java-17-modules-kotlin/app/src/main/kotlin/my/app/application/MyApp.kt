package my.app.application

import io.github.classgraph.ClassInfo
import my.app.module1.Module1
import my.app.module2.Module2

fun main() {
    Module2().use(Module1())
    println("Hello from App!")
    println("Info: ${ClassInfo::class.java.module}" )
}

