import java.text.SimpleDateFormat
import java.util.Date

version = providers.gradleProperty("timestamp")
    .orElse(SimpleDateFormat("yyyyMMddHHmmssSSS").format(Date()))
    .map {timestamp -> "1.0-${timestamp}" }
    .get()
