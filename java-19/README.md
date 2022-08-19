Build your code with an Early Access release of Java 19

- Install Early Access version of Java 19 using [SDKMan](https://sdkman.io): `sdk i java 19.ea.36-open`  
  (This is necessary, because Gradle can only auto-download [Eclipse Temurin JDK releases](https://adoptium.net/))
- Configure Gradle to [build with Java 19 JDK](gradle/plugins/java-plugins/src/main/kotlin/java19-library.gradle.kts#L6).  
  (Gradle will automatically discover the JDKs installed through SDKMan)
