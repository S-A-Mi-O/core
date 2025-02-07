
plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.samio"
version = "7.5.0"

repositories {
    mavenCentral()

    maven {
        url = uri("https://repo.spring.io/milestone")
    }
    maven {
        url = uri("https://repo.spring.io/snapshot")
    }

    google()
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-jakarta-xmlbind-annotations:2.18.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2")
    implementation("com.google.guava:guava:33.2.1-jre")
    implementation ("io.github.cdimascio:dotenv-kotlin:6.2.2")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.0")
    implementation("org.hibernate:hibernate-core:6.6.3.Final")
    implementation("org.reflections:reflections:0.10.2")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    implementation ("jakarta.validation:jakarta.validation-api:3.0.2")
    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
    testImplementation(libs.junit.jupiter)
    testImplementation("org.assertj:assertj-core:3.27.2")
    testImplementation("org.mockito:mockito-core:5.0.0")
    api(libs.commons.math3)
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
    implementation ("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    //Fixme
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.4")
    //Fixme
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.4")
    //Fixme
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.4")
    implementation("org.springframework.kafka:spring-kafka:3.2.4")
    implementation("org.springframework:spring-orm:6.1.13")
    implementation("org.springframework:spring-tx:6.1.13")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

fun loadEnv(): Map<String, String> {
    val envFile = file("${rootProject.projectDir}/.env")
    if (!envFile.exists()) {
        throw GradleException(".env file not found")
    }

    return envFile.readLines()
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .map { it.split("=", limit = 2) }
        .associate { it[0] to it.getOrElse(1) { "" } }
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = "com.github.S-A-Mi-O"
            artifactId = "core"
            version = project.version.toString()
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/S-A-Mi-O/core")
            credentials {
                val env = loadEnv()
                username = env["GITHUB_USERNAME"] ?: ""
                password = env["GITHUB_TOKEN"] ?: ""
            }
        }
    }
}
