plugins {
    id("java-library")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(19))
}

testing {
    suites.getByName<JvmTestSuite>("test").useJUnitJupiter()
}
