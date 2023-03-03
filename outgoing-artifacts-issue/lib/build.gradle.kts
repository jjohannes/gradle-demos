import org.example.plugin.GenerateFiles

plugins {
    id("dummy")
}

// val outFiles = objects.listProperty<RegularFile>()
// outFiles.add(layout.buildDirectory.file("1.txt"))
// outFiles.add(layout.buildDirectory.file("2.txt"))

val gen = tasks.register<GenerateFiles>("gen") {
    content.set("foo")
    outputFiles.add(layout.buildDirectory.file("1.txt"))
    outputFiles.add(layout.buildDirectory.file("2.txt"))

    // outFiles.set(outFiles)
}

configurations.create("out") {
    isCanBeResolved = false
    isCanBeConsumed = true
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named("gen"))
    outgoing.artifacts(gen.flatMap { it.outputFiles })

    //outgoing.artifacts(outFiles) { builtBy(gen) }
}
