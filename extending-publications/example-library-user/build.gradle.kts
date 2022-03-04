plugins {
    id("java-library")
}

repositories {
    maven("../example-repo")
}

val docs = configurations.create("docs") {
    isCanBeResolved = true
    isCanBeConsumed = false
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named("readme"))
    }
}

dependencies {
    // This one declaration will give us all the artifacts of 'library1' and its dependencies ('library2' in the example)
    docs("com.example:library1:1.0")

    // Old/Maven way without Gradle Metadata - you can ony address each artifact individually using @type notation (or classifiers for other Jars):
    //   docs("com.example:library1:1.0@MD")
    //   docs("com.example:library1:1.0@txt")
    //   docs("com.example:library2:1.0@MD")
    //   docs("com.example:library2:1.0@txt")
}

tasks.register("printReadmes") {
    doLast {
        docs.files.forEach{
            println(it.name)
            println("===========")
            println(it.readText())
            println()
        }
    }
}