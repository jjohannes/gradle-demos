pluginManagement {
    includeBuild("..")
}
plugins {
    id("software.onepiece.toolchain-management")
}

tools {
    repository("https://repo.maven.apache.org/maven2/")

    register("JETTY", "org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip", "jetty-distribution-9.4.56.v20240826/bin/jetty.sh")
    register("KARAF", "org.apache.karaf", "apache-karaf", "4.4.6@zip", "apache-karaf-4.4.6/bin/status")
}
