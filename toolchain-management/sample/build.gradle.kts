tasks.register<ToolBasedExec>("runJetty") {
    toolId = "JETTY"
    group = "build"
    isIgnoreExitValue = true
}

tasks.register<software.onepiece.toolchain.tasks.PrintToolInfo>("printKaraf") {
    toolId = "KARAF"
    group = "build"
    result = layout.buildDirectory.file("k-out.txt")
}


abstract class ToolBasedExec : Exec(), software.onepiece.toolchain.ToolUsingTask {
    override fun exec() {
        val tool = toolInstall.get().getTool(toolId.get())
        setExecutable(tool.installationDirectory.file(tool.executable).get().asFile)
        super.exec()
    }
}
