plugins {
    id("java")
    id("org.gradlex.java-module-testing")
    id("org.gradlex.java-module-packaging")
}

javaModulePackaging {
    multiTargetTestSuite(testing.suites["test"])
}
