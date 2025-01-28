pluginManagement {
    includeBuild("..")
}

// tools {
//     repository("https://repo.maven.apache.org/maven2/")
//
//     register("JETTY", "org.eclipse.jetty", "jetty-distribution", "9.4.56.v20240826@zip") {
//         executable = "jetty-distribution-9.4.56.v20240826/bin/jetty.sh"
//         excludes.add(".*/jetty-http2.xml")
//         fromFolder = "/Users/jendrik/jetty"
//     }
//     register("KARAF", "org.apache.karaf", "apache-karaf", "4.4.6@zip") {
//         executable = "apache-karaf-4.4.6/bin/status"
//     }
