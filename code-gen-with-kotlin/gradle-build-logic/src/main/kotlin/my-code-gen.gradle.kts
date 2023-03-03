import de.jjohannes.codegen.GenerateCodeTask

task<GenerateCodeTask>("generateCode") {
    group = "build"
    conf.projectName.set("FunThings")
    conf.projectPackage.set("org.example")
    conf.initialCycleCount.set(99)

    targetDir.convention(layout.projectDirectory.dir("fun-things"))
}