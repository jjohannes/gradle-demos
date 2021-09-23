plugins {
    id("java-library")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}

tasks.test {
    useJUnitPlatform()
}