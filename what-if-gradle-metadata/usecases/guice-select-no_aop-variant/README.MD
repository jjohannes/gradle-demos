## What's the use case?

Guice publishes two variants of its byte code: the main variant, and the `no_aop` variant with references to _aop_ types removed.
The `no_aop` variant is published as a separate jar with the `no_aop` classifier.
In POM, you can not express that an artifact with a different classifier has been published and you cannot express relationships between the variants.
But in fact, the Guice variants have different dependencies and are mutually exclusive (both cannot be expressed in POM)

## How is it solved?

When publishing with Gradle Module Metadata, all variants are explicitly declared in the metadata.
Using capabilities, a variant can be selected by requesting a capability.
Furthermore, each variant has its own list of dependencies.
Using this, the `no_aop` variants do not declare the `aopalliance:aopalliance` dependency anymore.
Also, Gradle detects if both variants are in the dependency graph and reports the conflict - [guice-variant-conflict](../guice-variant-conflict).

**NOTE:** Guice has a transitive dependency to Guava, so the effects of a reduced runtime classpath solved by [guava-compile-only-dependencies](../guava-compile-only-dependencies) are also seen here. 