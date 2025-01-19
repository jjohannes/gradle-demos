plugins {
    id("software.onepiece.toolchain-management")
}

tools {
    register("JETTY", "org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip", "jetty-distribution-9.4.56.v20240826/bin/jetty.sh")
    register("KARAF", "org.apache.karaf", "apache-karaf", "4.4.6@zip", "apache-karaf-4.4.6/bin/status")
}

tasks.register<ToolBasedExec>("runJetty") {
    toolId = "JETTY"
    group = "build"
    isIgnoreExitValue = true
}

tasks.register<PrintTool>("printKaraf") {
    toolId = "KARAF"
    group = "build"
    result = layout.buildDirectory.file("k-out.txt")
}


abstract class ToolBasedExec : Exec(), software.onepiece.toolchain.ToolUsingTask {
    override fun exec() {
        val tool = toolInstall.get().getTool(toolId.get(), fileSystemAccess)
        setExecutable(tool.installationDirectory.file(tool.executable).get().asFile)
        super.exec()
    }
}

abstract class PrintTool : DefaultTask(), software.onepiece.toolchain.ToolUsingTask {

    @get:OutputFile
    abstract val result: RegularFileProperty

    @TaskAction
    protected fun execute() {
        val tool = toolInstall.get().getTool(toolId.get(), fileSystemAccess)

        result.get().asFile.writeText("Did something with " + tool.executable.get())
    }
}

