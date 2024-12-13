plugins {
    id("org.example.tool-installation")
}

tools {
    register("JETTY", "org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip")
    register("KARAF", "org.apache.karaf", "apache-karaf", "4.4.6@zip")
}

tasks.register<Exec>("runJetty") {
    inputs.files(tools.byId("JETTY"))

    group = "build"
    executable = File(inputs.files.singleFile, "jetty-distribution-9.4.56.v20240826/bin/jetty.sh").absolutePath
    isIgnoreExitValue = true
}

