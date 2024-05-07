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

val excifyVersion = "0.0.2+"
val arrowKtVersion = "1.2.4+"
val jacksonModuleVersion = "2.17.1+"

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("com.github.Mixfaa:excify:$excifyVersion")

    implementation("io.arrow-kt:arrow-core:$arrowKtVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")
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