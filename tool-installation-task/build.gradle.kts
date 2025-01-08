import org.example.tasks.ToolBasedExec

plugins {
    id("org.example.tool-installation")
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
