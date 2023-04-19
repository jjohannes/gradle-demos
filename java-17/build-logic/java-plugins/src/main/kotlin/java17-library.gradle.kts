plugins {
    id("java-library")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}