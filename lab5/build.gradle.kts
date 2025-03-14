plugins {
    id("java")
    id("io.freefair.lombok") version "8.12"
}

group = "se.ifmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.16.1")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("io.vertx:vertx-core:4.5.3")
}

tasks.register<JavaExec>("runApp") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("se.ifmo.Main")
    standardInput = System.`in`

    environment("LAB5_DATA_PATH", "ext/data.csv") // <-- Передаём в процесс
    environment("INDEX", "ext/INDEX")
    environment("ERROR_LOG", "ext/ERROR_LOG")
}
