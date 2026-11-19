import org.example.DecisionTask
import org.example.DeltaTask
import org.example.FullTask
import org.example.incremental.IncrementalTask

val decisionTask = tasks.register<DecisionTask>("decision") {
    changedFile = layout.projectDirectory.file("changed.txt")

    deltaBuildDecision = layout.buildDirectory.file("intermediate/deltaBuildDecision.txt")
    fullBuildDecision = layout.buildDirectory.file("intermediate/fullBuildDecision.txt")
}

val full = tasks.register<FullTask>("full") {
    decision = decisionTask.flatMap { it.fullBuildDecision }
    outputFile = layout.buildDirectory.file("full_result.txt")
}

val delta = tasks.register<DeltaTask>("delta") {
    decision = decisionTask.flatMap { it.deltaBuildDecision }
    outputFile = layout.buildDirectory.file("delta_result.txt")
}

tasks.register("build") {
    dependsOn(delta, full)
}


// Incremental task
val incremental = tasks.register<IncrementalTask>("incremental") {
    sourceFiles.from(layout.projectDirectory.dir("src"))
    outDir = layout.buildDirectory.dir("incremental")
}

tasks.register<Sync>("syncAfterIncremental") {
    from(incremental)
    into(layout.buildDirectory.dir("finalDestination"))
}
