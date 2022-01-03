plugins {
    id("aggregated.publish.aggregated-publish")
}

group = "org.example.mylibs"
version = "1.2"

publishingComponents {
    register("utilities")
    register("list")
}

publishing {
    repositories.maven("../repo")
}