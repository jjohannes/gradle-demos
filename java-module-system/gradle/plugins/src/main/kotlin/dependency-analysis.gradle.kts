import org.gradlex.javamodule.dependencies.tasks.ModuleDirectivesScopeCheck

// Analyze scopes of dependency definitions as part of 'check'
plugins {
    id("base")
    id("com.autonomousapps.dependency-analysis")
}

tasks.check {
    dependsOn(tasks.withType<ModuleDirectivesScopeCheck>())
}
