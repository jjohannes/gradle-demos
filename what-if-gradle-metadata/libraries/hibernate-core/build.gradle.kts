plugins {
    `java-library`
    `maven-publish`
}
repositories {
    mavenCentral()
}

group = "org.hibernate.orm"
version = findProperty("hibernateVersion")?: "6.0.0.Alpha2"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // some of the dependencies can probably be declared as 'implementation' (only 'runtime classpath variant' in Gradle Module Metadata; 'runtime' scope in POM)
    api("org.jboss.logging:jboss-logging:3.3.2.Final")
    api("javax.persistence:javax.persistence-api:2.2")
    api("org.javassist:javassist:3.24.0-GA")
    api("net.bytebuddy:byte-buddy:1.9.5")
    api("org.antlr:antlr4:4.7.1")
    api("org.jboss.spec.javax.transaction:jboss-transaction-api_1.2_spec:1.1.1.Final")
    api("org.jboss:jandex:2.0.5.Final")
    api("com.fasterxml:classmate:1.3.4")
    api("javax.activation:javax.activation-api:1.2.0")
    api("org.dom4j:dom4j:2.1.1")
    api("org.hibernate.common:hibernate-commons-annotations:5.1.0.Final")
    api("javax.xml.bind:jaxb-api:2.3.1")
    api("org.glassfish.jaxb:jaxb-runtime:2.3.1")

    constraints {
        // hibernate-c3p0, if present, should align to my version
        api("org.hibernate.orm:hibernate-c3p0:${project.version}")
    }
}

publishing {
    publications {
        create<MavenPublication>("hibernate-core") {
            from(components["java"])
        }
    }
}
