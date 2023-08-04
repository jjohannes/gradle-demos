import software.onepiece.gradle.metadatarules.RemoveAllDependencyVersionsRule

plugins {
    id("application")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

dependencies.components {
    all<RemoveAllDependencyVersionsRule>()
}