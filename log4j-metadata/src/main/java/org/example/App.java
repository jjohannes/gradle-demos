package org.example;

public class App {
    org.apache.logging.log4j.message.ParameterizedMessage m; // needs errorprone
    org.apache.logging.log4j.status.StatusData d; // needs spotbugs
    org.apache.logging.log4j.util.SystemPropertiesPropertySource s; // needs aQute.bnd
    org.apache.logging.log4j.util.Activator a; // needs osgi
    org.apache.logging.log4j.spi.LoggerRegistry<?> r; // needs jspecify

    public static void main(String[] args) {
        System.out.println("Hello from: " + App.class.getModule().getName());
    }
}
