
val gen = configurations.create("gen") {
    isCanBeConsumed = false
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named("gen"))
}

dependencies {
    "gen"(project(":lib"))
}

tasks.register("print") {
    doLast {
        gen.forEach { println(it.name) }
    }
}
