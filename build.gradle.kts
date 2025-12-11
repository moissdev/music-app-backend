plugins {
    kotlin("jvm") version "2.1.20-Beta1"
    id("io.ktor.plugin") version "3.0.0"
    kotlin("plugin.serialization") version "2.1.20-Beta1"
}

group = "com.moissdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// Versiones de las librerías
val exposed_version = "0.50.1"
val h2_version = "2.2.224"
val postgres_version = "42.7.2"
val hikari_version = "5.1.0"
val logback_version = "1.4.14"
val ktor_version = "3.0.1"

dependencies {
    // --- KTOR ---
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // --- BASE DE DATOS (EXPOSED + POSTGRES) ---
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // Driver Postgres y Pool
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.zaxxer:HikariCP:$hikari_version")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logback_version")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}