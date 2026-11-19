# What if...
...Gradle Module Metadata would have existed when your favorite JVM library was published .

A demonstration of how Gradle Module Metadata could look like for a number of famous open-source JVM libraries.

## How to use?

- Run [publish.sh](publish.sh) to fill the *repository* folder with versions of libraries with Gradle Module Metadata added.
  The artifacts (jars) are the original artifacts taken from Maven Central.
  You can inspect the [libraries](libraries) folder that contains the Gradle builds re-defining dependencies and other properties of the libraries to generate the metadata.
- Go into the [usecases](usecases) folder and inspect the Gradle builds in there to see how different issues are now solved.  
- To see the resulting classpath (or conflict) run `./gradlew :<use-case-folder-name>:checkClasspath` or  `./gradlew :<use-case-folder-name>:dependencise` inside the [usecases](usecases) folder.
  You can also run with the `--scan` option to inspect the dependencies in a [build scan](https://gradle.com/build-scans/).
