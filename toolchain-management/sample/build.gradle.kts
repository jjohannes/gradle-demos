tasks.register<ToolBasedExec>("runJetty") {
    toolIds.add("JETTY")
    group = "build"
    isIgnoreExitValue = true
}

tasks.register<software.onepiece.toolchain.tasks.PrintToolInfo>("printKaraf") {
    toolIds.add("KARAF")
    group = "build"
    result = layout.buildDirectory.file("k-out.txt")
}


abstract class ToolBasedExec : Exec(), software.onepiece.toolchain.ToolUsingTask {
    override fun exec() {
        val tools = toolInstall.get().getTools(toolIds.get())
        setExecutable(tools.single().installationDirectory.file(tools.single().executable).get().asFile)
        super.exec()
    }
}
