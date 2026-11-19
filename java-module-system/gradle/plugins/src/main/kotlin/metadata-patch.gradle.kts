
plugins {
    id("org.gradlex.jvm-dependency-conflict-resolution")
    id("org.gradlex.extra-java-module-info")
}

// Patch the Maven/Gradle metadata (*.pom and *.module files)
jvmDependencyConflicts {
    patch {
        module("com.google.guava:guava") {
            removeDependency("com.google.code.findbugs:jsr305")
            removeDependency("org.checkerframework:checker-qual")
            removeDependency("com.google.errorprone:error_prone_annotations")
        }
        listOf("base", "graphics", "controls").forEach { jfxModule ->
            module("org.openjfx:javafx-$jfxModule") {
                addTargetPlatformVariant("", "none", "none")
                addTargetPlatformVariant("linux", OperatingSystemFamily.LINUX, MachineArchitecture.X86_64)
                addTargetPlatformVariant("linux-aarch64", OperatingSystemFamily.LINUX, MachineArchitecture.ARM64)
                addTargetPlatformVariant("mac", OperatingSystemFamily.MACOS, MachineArchitecture.X86_64)
                addTargetPlatformVariant("mac-aarch64", OperatingSystemFamily.MACOS, MachineArchitecture.ARM64)
                addTargetPlatformVariant("win", OperatingSystemFamily.WINDOWS, MachineArchitecture.X86_64)
            }
        }
    }
}

// Patch the Java Module System metadata (module-info.class in Jar file)
extraJavaModuleInfo {
    module("com.google.guava:guava", "com.google.common")
    module("com.google.guava:failureaccess", "com.google.common.util.concurrent.internal")
}
