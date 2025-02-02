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
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")
    implementation("io.vertx:vertx-core:4.5.3")
}

tasks.register<JavaExec>("runApp") {
    classpath = sourceSets.main.get().runtimeClasspath
    //mainClass.set("se.ifmo.Main")
    mainClass.set("se.ifmo.system.collection.CollectionManager")

    environment("LAB5_DATA_PATH", "src/data.xml") // <-- Передаём в процесс
    environment("INDEX", "src/INDEX")

    doFirst {
        println("LAB5_DATA_PATH = ${environment["LAB5_DATA_PATH"]}")
        println("INDEX = ${environment["INDEX"]}")
    }
}
