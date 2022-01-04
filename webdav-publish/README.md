Add uploading through Maven Wagons (e.g. for WebDAV) to Gradle's `maven-publish` mechanism.

See also: https://github.com/gradle/gradle/issues/14750#issuecomment-784358228

To try:
- Configure WebDAV url and credentials in [my-library/build.gradle.kts](my-library/build.gradle.kts)
- Run `./gradlew :my-library:publish`
