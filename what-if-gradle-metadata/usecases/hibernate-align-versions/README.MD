## What's the use case?

Hibernate 6 provides integrations with many database libraries.
These integrations, such as _hibernate-c3p0_, depend on _hibernate-core_ and the versions should always be aligned.
However, there is no possibility to express this in a POM for all cases.
That is, if _hibernate-core_ is upgraded, and _hibernate-c3p0_ is used as well, _hibernate-c3p0_ should be upgraded to the same version as _hibernate-core_.

## How is it solved?

Gradle Module Metadata allows you to publish _dependency constraints_.
This allows _hibernate-core_ to publish a constraint on _hibernate-c3p0_ expressing that the version of _hibernate-c3p0_, if it is used, has to be the same as the version of _hibernate-core_.
The constraint only takes effect when _hibernate-c3p0_ is also a dependency (otherwise it is ignored).
This is similar to the [Jackson alignment use case](../jackson-align-versions), which uses a platform/BOM that carries all the constraints.
Only that in this case, we do not publish a separate platform/BOM but let individual modules publish their constraints directly (which is the same solution as in the [AndroidX use case](../androidx-align-versions)).