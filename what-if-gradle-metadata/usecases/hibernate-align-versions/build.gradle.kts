plugins {
    `java-library`
}

repositories {
    maven { setUrl(rootProject.file("../repository")) } // <- repository with Gradle Module Metadata added
    mavenCentral()
}

dependencies {
    implementation("org.hibernate.orm:hibernate-c3p0:6.0.0.Alpha1") // should automatically align with core to 6.0.0.Alpha2
    implementation("org.hibernate.orm:hibernate-core:6.0.0.Alpha2")
}
