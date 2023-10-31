plugins {
    id("com.example.product.base")
    id("application")
}

group = "com.example.product.application"

val app = extensions.create<ProductApplicationExtension>("app")

val main = configurations.dependencyScope("main")
val mainRuntimeClasspath = configurations.resolvable("mainRuntimeClasspath") {
    extendsFrom(main.get())
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    incoming.afterResolve {
        val frameworkMain = (resolutionResult.root.dependencies.first() as ResolvedDependencyResult).resolvedVariant
        val expectedPlugins = frameworkMain.attributes.getAttribute(Versions.PLUGINS_VERSION_ATTRIBUTE)!!
        Versions.check(expectedPlugins, logger)
    }
}
dependencies.addProvider(main.name, app.frameworkVersion.map {
    "com.example.product.framework:main:$it"
})

sourceSets.all {
    configurations[compileClasspathConfigurationName].shouldResolveConsistentlyWith(mainRuntimeClasspath.get())
    configurations[runtimeClasspathConfigurationName].shouldResolveConsistentlyWith(mainRuntimeClasspath.get())
}

application {
    mainModule = "com.example.product.${project.name}"
    mainClass = "com.example.product.${project.name}.Main"
}