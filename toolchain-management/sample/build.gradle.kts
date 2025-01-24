import software.onepiece.toolchain.worker.ToolUsingWorkAction
import software.onepiece.toolchain.task.ToolUsingTask
import software.onepiece.toolchain.task.PrintToolInfo

tasks.register<ToolBasedExec>("runJetty") {
    toolIds.add("JETTY")
    group = "build"
    isIgnoreExitValue = true
}

tasks.register<PrintToolInfo>("printKaraf") {
    toolIds.add("KARAF")
    group = "build"
    result = layout.buildDirectory.file("k-out.txt")
}

tasks.register<PrintToolInfo>("printJETTY") {
    toolIds.add("JETTY")
    group = "build"
    result = layout.buildDirectory.file("j-out.txt")
}

tasks.register<PrintToolInfo>("printJETTY2") {
    toolIds.add("JETTY")
    group = "build"
    result = layout.buildDirectory.file("j2-out.txt")
}


tasks.register("all") {
    dependsOn("printKaraf")
    dependsOn("printJETTY")
    dependsOn("printJETTY2")
}


abstract class ToolBasedExec : Exec(), ToolUsingTask {

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
