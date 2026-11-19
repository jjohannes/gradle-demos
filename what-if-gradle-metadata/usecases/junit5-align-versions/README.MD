**NOTE:**

**Junit 5.6.0-M1 publishes Gradle Module Metadata** (this is no longer a what-if!)

## What's the use case?

JUnit 5 is published as a set of modules.
A user can choose to only use a subset, but the versions of all the chosen JUnit 5 modules, and the ones required transitively, should be aligned.
To help with this, JUnit 5 publishes a BOM containing all the information required for version alignment.
However, with POM metadata, it is not possible for a module to state that the BOM has to be used.
Each consumer of JUnit 5 has to import the BOM explicitly.

## How is it solved?

Gradle Module Metadata allows you to define _platform_ dependencies.
A platform provides dependency constraints, but no artifacts and usually also no dependencies (although that is also supported).
A BOM can be treated as a platform.
If each JUnit 5 module adds a platform dependency to the junit-bom, `api(platform(project(":junit-bom")))`, it is automatically considered if any module of JUnit 5 is used and versions are always aligned.
