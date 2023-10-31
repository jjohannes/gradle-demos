plugins {
    id("java")
    id("org.gradlex.java-module-dependencies")
}

javaModuleDependencies {
    moduleNamePrefixToGroup.put("com.example.product.framework.", "com.example.product.framework")
}
