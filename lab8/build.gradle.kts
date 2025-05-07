plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("io.freefair.lombok") version "8.12"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-controls:17.0.2")
    implementation("org.openjfx:javafx-fxml:17.0.2")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("commons-codec:commons-codec:1.15")
    implementation("org.postgresql:postgresql:42.7.5")
}

application {
    mainClass.set("se.ifmo.client.Main")
}

javafx{
    version = "23.0.1"
    modules("javafx.controls", "javafx.fxml")
}

tasks.register<Jar>("clientFatJar") {
    archiveClassifier.set("client")
    manifest {
        attributes["Main-Class"] = "se.ifmo.client.Main"
    }
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Jar>("serverFatJar") {

    archiveClassifier.set("server")
    manifest {
        attributes["Main-Class"] = "se.ifmo.server.ServerMain"
    }
    from(sourceSets.main.get().output) {
        exclude("se/ifmo/client/**")
    }
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}