import software.onepiece.toolchain.task.PrintToolInfo
import software.onepiece.toolchain.task.ToolUsingTask

plugins {
    id("software.onepiece.tool-install") // to load task and service classes
}

tasks.withType<ToolUsingTask>().configureEach {
    toolRepository("https://repo.maven.apache.org/maven2/")
}

tasks.register<PrintToolInfo>("printKaraf") {
    tool("org.apache.karaf", "apache-karaf", "4.4.6@zip") {
        executable = "apache-karaf-4.4.6/bin/status"
    }
    result = layout.buildDirectory.file("k-out.txt")
}

tasks.register<PrintToolInfo>("printJETTY") {
    tool("org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip") {
        executable = "jetty-distribution-9.4.56.v20240826/bin/jetty.sh"
    }
    result = layout.buildDirectory.file("j-out.txt")
}

tasks.register("all") {
    dependsOn("printKaraf")
    dependsOn("printJETTY")
}

tasks.register<ToolBasedExec>("runJetty") {
    tool("org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip") {
        executable = "jetty-distribution-9.4.56.v20240826/bin/jetty.sh"
    }
    isIgnoreExitValue = true
}

abstract class ToolBasedExec : Exec(), ToolUsingTask {

    override fun exec() {
        installTools()

        val singleTool = tools.get().single()
        setExecutable(singleTool.installationDirectory.file(singleTool.executable).get().asFile)
        super.exec()
    }
}
