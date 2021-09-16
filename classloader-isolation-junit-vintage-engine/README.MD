# Sample

Implement your own JUnit vintage engine and use it in Gradle.
The implementation delegates to the standard vintage engine, but re-loads 
each test class, ad through that all 'own code', in an isolated class loader.
