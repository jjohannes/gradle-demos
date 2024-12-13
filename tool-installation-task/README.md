# Tool Installation Task

Extract tool Zip files through a tool installation task that make sure the extraction is not done again,
even though Gradle's standard UP-TO-DATE check re-runs the task due to changes on Gradle's plugin
classpath (which is considered a task implementation change).
