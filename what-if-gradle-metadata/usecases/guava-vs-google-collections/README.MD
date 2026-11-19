## What's the use case?

In the past, the collection part of Guava was published under different coordinates `com.google.collections:google-collections`.
In POM metadata, such a relationship between modules, i.e. one replaces the other and both cannot be used together, cannot be expressed.

## How is it solved?

This is similar to the [guava-listenablefuture-conflict](../guava-listenablefuture-conflict) use case and solved in a similar manner by letting Guava express that it provides the `com.google.collections:google-collections` capability.