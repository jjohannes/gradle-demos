subprojects {
    tasks.create("checkClasspath") {
        doLast {
            val compile = configurations["compileClasspath"].map { it.name }
            val rt = configurations["runtimeClasspath"].map { it.name }
            println("=== COMPILE ===")
            compile.forEach { println(it) }
            println("=== RUNTIME ===")
            rt.forEach { println(it) }
        }
    }
}