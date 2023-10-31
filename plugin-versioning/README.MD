# Version check for product-specific Gradle plugins

## Scenario

We are developing a software product consisting of multiple applications based on a _framework_.
For _framework_ and application development we have a number of custom Gradle plugins for common conventions.
The plugins evolve as new requirements for the applications arise which also reflect in changes to the _framework_.
Thus, if changing the framework version for an application, also the Gradle plugins versions should be updated if required.

## Solution

This example shows how to add such a version check to the product-specific plugins.

It consists of three Gradle builds:
- [gradle-plugins](gradle-plugins) - Our product-specific plugins, which are published to a repository and used from there by the other two builds
- [framework](framework) - The _framework_ consisting of multiple modules that are versioned and published to a repository 
- [application.a](application.a) - One application that uses _framework_; the framework version to use is defined in the `build.gradle.kts` through a custom extension

The version check mechanism is implemented as follows:
- In [Versions.kt](gradle-plugins/src/main/kotlin/Versions.kt),
  we define a precise Java and Gradle version with which the plugins are expected to be used.
- In [com.example.product.framework.main.gradle.kts](gradle-plugins/src/main/kotlin/com.example.product.framework.main.gradle.kts)
  (convention plugin for _framework_ main component), we let the build fail if the Gradle or Java version is wrong.
  This ensures that the framework is built using the versions defined in the _gradle-plugins_.
- In [com.example.product.framework.main.gradle.kts](gradle-plugins/src/main/kotlin/com.example.product.framework.main.gradle.kts),
  we also add the current _gradle-plugins_ version as an attribute to the metadata of the framework main component.
- In [com.example.product.application.gradle.kts](gradle-plugins/src/main/kotlin/com.example.product.application.gradle.kts),
  the framework main component is referenced for consistent resolution.
  Through this, we get the correct versions for all framework modules that we use.
  After the metadata has been fetched - `afterResolve` - we check for the _gradle-plugins_ version and print a warning if it does not match.
  If a developer of an application changes the framework version and the new version was built with a different version of _gradle-plugins_, the developer is informed.

## How to try

To see this example in action, you can publish the _gradle-plugins_ and _framework_ into local folders that act as repositories.
You can publish multiple versions by modifying [framework/version.txt](framework/version.txt) (for framework) or the version in [gradle-plugins/build.gradle.kts](gradle-plugins/build.gradle.kts) (for gradle-plugins).

```shell
 ./application.a/gradlew -p framework publish
```

```shell
 ./application.a/gradlew -p gradle-plugins publish
```
