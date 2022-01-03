Gradle's current publishing mechanism publishes each (sub)project independently.

This is an experiment exploring how all information can first be aggregated in one project to be published in one go from there.

With this solution, publishing is done in "one project", but not yet in one task.

Run: `./gradlew :publish:publish`