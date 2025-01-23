import software.onepiece.toolchain.ToolUsingWorkAction

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

tasks.register<software.onepiece.toolchain.tasks.PrintToolInfo>("printJETTY") {
    toolIds.add("JETTY")
    group = "build"
    result = layout.buildDirectory.file("j-out.txt")
}

tasks.register<software.onepiece.toolchain.tasks.PrintToolInfo>("printJETTY2") {
    toolIds.add("JETTY")
    group = "build"
    result = layout.buildDirectory.file("j2-out.txt")
}


tasks.register("all") {
    dependsOn("printKaraf")
    dependsOn("printJETTY")
    dependsOn("printJETTY2")
}


abstract class ToolBasedExec : Exec(), software.onepiece.toolchain.ToolUsingTask {

    @get:Inject
    abstract val executor: WorkerExecutor

    override fun exec() {
        executor.noIsolation().submit(ToolUsingWorkAction.Default::class.java) {
            toolInstall = this@ToolBasedExec.toolInstall
            toolIds = this@ToolBasedExec.toolIds
        }
        executor.await()

        val tools = toolInstall.get().getTools(toolIds.get())
        setExecutable(tools.single().installationDirectory.file(tools.single().executable).get().asFile)
        super.exec()
    }
}
