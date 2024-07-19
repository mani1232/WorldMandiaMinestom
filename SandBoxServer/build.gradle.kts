import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
}

kotlin {
    jvmToolchain(21)
}

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
        archiveFileName.set("${project.name}-$version.jar")
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
