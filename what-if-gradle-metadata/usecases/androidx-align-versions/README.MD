## What's the use case?

[AndroidX](https://developer.android.com/jetpack/androidx) provides a set of libraries for Android development directly from the Android team.
There are a number of _androidx_ packages that are maintained and versioned separately.
However, one package may consist of several modules (for example, the core package contains _core_ and _core-kts_).
There can be version constraints between those modules, and also between modules of different packages in case of major version updates (the packages follow semantic versioning).
These constraints cannot always be expressed in POM.
For example, _core-kts_ depends on _core_ and they are released and versioned together.
But there is no possibility to express, in POM, that the versions of these two always have to be aligned.
That is, if _core_ is upgraded, and _core-kts_ is used as well, _core-kts_ should be upgraded to the same version as _core_.

## How is it solved?

Gradle Module Metadata allows you to publish _dependency constraints_.
This allows _core_ to publish a constraint on _core-kts_ expressing that the version of _core-kts_, if it is used, has to be the same as the version of _core_.
The constraint only takes effect when _core-kts_ is also a dependency (otherwise it is ignored).
This is similar to the [Jackson alignment use case](../jackson-align-versions), which uses a platform/BOM that carries all the constraints.
Only that in this case, we do not publish a separate platform/BOM but let individual modules publish their constraints directly. 
