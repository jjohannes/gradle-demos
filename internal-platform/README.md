### How to use a platform without publishing it?

- [Video on Platforms](https://www.youtube.com/watch?v=8044F5gc1dE&list=PLWQK2ZdV4Yl2k2OmC_gsjDpdIBTN0qqkE&t=174s)
- [Gradle User Manual on Platforms](https://docs.gradle.org/current/userguide/platforms.html#sub:using-platform-to-control-transitive-deps)

Run `./gradlew publish` - The `repo` folder will not include a published platform.
Instead, versions of the dependencies can be found directly in the metadata of the 'lib-a' and 'lib-b' components.
