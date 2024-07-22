import io.ktor.plugin.features.*
import proguard.gradle.ProGuardTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
}

kotlin {
    jvmToolchain(21)
}

val defaultFileName = "${project.name}-$version.jar"

dependencies {
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.other.klogging.slf4j)
    implementation(libs.other.cache)
    implementation(libs.other.cache.file)
    implementation(libs.other.minestomCoroutines.core)
    implementation(libs.other.minestomCoroutines.api)
    implementation(libs.other.kaml)
    implementation(libs.other.polar)
    implementation(libs.kotlin.reflect)
    implementation(libs.minestom)
}

ktor {
    fatJar {
        archiveFileName.set(defaultFileName)
    }
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("sandboxserver-local")
        imageTag.set(version.toString())
        customBaseImage.set("azul/zulu-openjdk:21-latest")

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "sandboxserver" },
                username = provider { "mani12322" },
                password = provider { "dckr_pat_iekI2Z2uTFIKo9dCjrPy5kzmY6E" }
                //username = providers.environmentVariable("DOCKER_NAME"),
                //password = providers.environmentVariable("DOCKER_SECRET")
            )
        )
        jib {
            container {
                workingDirectory = "/home/container"
            }
        }
    }
}

application {
    mainClass.set("cc.worldmandia.MainKt")
}

tasks {
    shadowJar {
        relocate("org.jetbrains.kotlinx", "cc.worldmandia.libs.kotlinx")
        relocate("io.klogging", "cc.worldmandia.libs.klogging")
        relocate("com.mayakapps.kache", "cc.worldmandia.libs.kache")
        relocate("com.github.shynixn.mccoroutine", "cc.worldmandia.libs.mccoroutine")
        relocate("com.charleskorn.kaml", "cc.worldmandia.libs.kaml")
        relocate("dev.hollowcube", "cc.worldmandia.libs.polar")
        relocate("org.jetbrains.kotlin", "cc.worldmandia.libs.kotlin")
        relocate("net.minestom", "cc.worldmandia.libs.minestom")
    }
}

tasks.register<ProGuardTask>("obfuscate") {
    configuration("proguard-rules.pro")

    val file = (tasks["shadowJar"] as Jar).archiveFile

    injars(file)

    verbose()

    project.delete(layout.buildDirectory.file("libs/$defaultFileName"))

    outjars(layout.buildDirectory.file("libs/$defaultFileName"))
}