import org.gradle.api.attributes.Attribute
import org.gradle.util.GradleVersion
import org.slf4j.Logger

object Versions {
    private const val EXPECTED_JAVA = "17.0.7"
    private const val EXPECTED_GRADLE = "8.4"

    private const val PLUGINS_JAR = "gradle-plugins"

    val PLUGINS_VERSION_ATTRIBUTE = Attribute.of("com.example.product.gradle.plugins.version", String::class.java)

    val ACTUAL_JAVA = System.getProperty("java.version")
    val ACTUAL_GRADLE = GradleVersion.current().version
    val ACTUAL_PLUGINS = PrintVersionInfo::class.java.protectionDomain.codeSource.location.path.let {
        it.substring(it.lastIndexOf("/$PLUGINS_JAR-") + "/$PLUGINS_JAR-".length, it.lastIndexOf(".jar"))
    }

    fun check(expectedPlugins: String, logger: Logger? = null) {
        var issue = ""
        if (ACTUAL_JAVA != EXPECTED_JAVA) {
            issue += "[VERSION JAVA]    Expected $EXPECTED_JAVA but was $ACTUAL_JAVA\n"
        }
        if (ACTUAL_GRADLE != EXPECTED_GRADLE) {
            issue += "[VERSION GRADLE]  Expected $EXPECTED_GRADLE but was $ACTUAL_GRADLE\n"
        }
        if (ACTUAL_PLUGINS != expectedPlugins) {
            issue += "[VERSION PLUGINS] Expected $expectedPlugins but was $ACTUAL_PLUGINS\n"
        }

        if (issue.isNotEmpty()) {
            if (logger == null) {
                throw RuntimeException(issue)
            } else {
                logger.error(issue)
            }
        }
    }
}