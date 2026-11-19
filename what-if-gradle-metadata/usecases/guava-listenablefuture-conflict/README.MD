## What's the use case?

In the past, a few classes were located in an external module `com.google.guava:listenablefuture`.
These classes are now part of Guava (maybe in reality they are part of `failureaccess`, but the solution would be similar).
In POM metadata, such a relationship between modules, i.e. one replaces the other and both cannot be used together, cannot be expressed.

## How is it solved?

In Gradle Module Metadata, a module, or more precise each variant of a module, can declare _capabilities_ it provides.
A _capability_ is identified by coordinates, and a module that does not declare any capability provides, by default, a capability matching the module coordinates.
Thus, Guava can now express that it provides the `com.google.guava:listenablefuture` capability.
The `com.google.guava:listenablefuture` **module** also provides that capability by default (even if it was not published with Gradle Module Metadata).
Now, if a version of the `listenablefuture` module appears in the same dependency graph as Guava, Gradle detects the conflict.
Because both provide the `com.google.guava:listenablefuture` capability.
