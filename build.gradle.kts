plugins {
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
}

group = "com.github.Mixfaa"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val excifyVersion = "0.0.1"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("com.github.Mixfaa:excify:$excifyVersion")

    implementation("io.arrow-kt:arrow-core:1.2.1")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
}

publishing {

    publications {
        register<MavenPublication>("excify-either-module") {
            from(components["kotlin"])
        }
    }
}

java {
    version = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}