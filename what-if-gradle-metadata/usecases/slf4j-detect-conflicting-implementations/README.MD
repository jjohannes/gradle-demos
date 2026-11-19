## What's the use case?

SLF4J offers a number of possibilities to integrate with other logging libraries.
While this is powerful, there is also a huge potential for conflict.
That is, many modules from SLF4J as well as other logging frameworks (e.g. `log4j:log4j`) cannot be used together.
These relationships between modules cannot be expressed in POM metadata.
Consequently, problems are usually only discovered at runtime.

## How is it solved?

In Gradle Module Metadata, a module, or more precise each variant of a module, can declare _capabilities_ it provides.
A _capability_ is identified by coordinates, and a module that does not declare any capability provides, by default, a capability matching the module coordinates.
This can be used to express that certain FLF4J modules cannot be used together, like two different binding such as `org.slf4j:slf4j-log4j12` and `org.slf4j:slf4j-simple`.
And also that certain SLF4J module cannot be used together with another logging module, like `org.slf4j:log4j-over-slf4j` and `log4j:log4j`
(this works even if `log4j:log4j` is not published with Gradle Module Metadata).
