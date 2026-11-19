rootProject.name = "what-if-gradle-metadata-libraries"

include("core")
rootProject.children.last().projectDir = file("androidx-core")

include("guava")

include("guice")

include("hibernate-core")

include("jackson-core")
include("jackson-annotations")
include("jackson-databind")

include("slf4j-api")
include("slf4j-simple")
include("slf4j-log4j12")
include("log4j-over-slf4j")
include("lwjgl")