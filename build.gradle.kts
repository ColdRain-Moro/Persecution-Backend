val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val ktorm_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.github.rain"
version = "0.0.1"

application {
    mainClass.set("io.github.rain.persecution.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    // mysql 方言
    implementation("org.ktorm:ktorm-support-mysql:${ktorm_version}")
    implementation("org.ktorm:ktorm-core:$ktorm_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    // 腾讯云对象存储sdk
    implementation("com.qcloud:cos_api:5.6.69")
    // mysql
    implementation("mysql:mysql-connector-java:8.0.28")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}