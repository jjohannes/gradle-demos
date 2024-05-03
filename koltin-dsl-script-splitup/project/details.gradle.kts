// -- Maybe this is needed to make the script work standalone, but would be great if it works without
// buildscript {
//     repositories { maven("../plugin-repo") }
//     dependencies { classpath("custom:plugin:1.0") }
// }

mydsl {
    registerStuff("bar")
}