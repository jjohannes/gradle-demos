## What's the use case?

Jackson is published as a set of modules.
A user can choose to only use a subset, but the versions of all the chosen Jackson modules, and the ones required transitively, should be aligned.
To help with this, Jackson publishes a BOM containing all the information required for version alignment.
However, with POM metadata, it is not possible for a module to state that the BOM has to be used.
Each consumer of Jackson has to import the BOM explicitly.

## How is it solved?

Gradle Module Metadata allows you to define _platform_ dependencies.
A platform provides dependency constraints, but no artifacts and usually also no dependencies (although that is also supported).
A BOM can be treated as a platform.
If each Jackson module adds a platform dependency to the Jackson BOM, `platform("com.fasterxml.jackson:jackson-bom:$version-of-module")`, it is automatically considered if any module of Jackson is used and versions are always aligned.
