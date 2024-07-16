import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.docker)
    alias(libs.plugins.docker.java)
}

group = "cc.worldmandia"

version = "1.0.0"

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
    implementation(libs.other.kstore)
    implementation(libs.other.kstore.file)
    //implementation(libs.other.kstore.storage)
    implementation(libs.kotlin.reflect)
    implementation(libs.minestom)
}

tasks {

}

val dockerHubUsername = providers.gradleProperty("DOCKER_HUB_USERNAME").get()
val imageName = "$dockerHubUsername/minestom-app"

docker {
    //url.set("https://hub.docker.com/")

}

ktor {
    fatJar {
        archiveFileName.set("${project.name}-$version.jar")
    }
}

val copyTask = tasks.register<Copy>("copyJarToTest") {
    from("build/libs/${project.name}-$version.jar")
    into("build/docker/")
}

tasks.register<Dockerfile>("createMyDockerfile") {
    dependsOn("buildFatJar")

    from("azul/zulu-openjdk:21")
    label(mapOf(
        "author" to "mani123",
    ))
    runCommand("apt-get update -y && " +
            "apt-get install -y lsof curl ca-certificates openssl git tar sqlite3 fontconfig libfreetype6 tzdata iproute2 libstdc++6 && " +
            "useradd -d /home/container -m container")
    user("container")
    environmentVariable("USER", "container")
    environmentVariable("HOME", "/home/container")
    workingDir("/home/container")
    copyFile("./entrypoint.sh", "/entrypoint.sh")
    copyFile("./${project.name}-$version.jar", "/app.jar")
    defaultCommand("/bin/bash", "/entrypoint.sh")
}

tasks.register<DockerBuildImage>("buildMyImage") {
    dependsOn("createMyDockerfile")
    images.add("$imageName:$version")
}

tasks.register<DockerPushImage>("pushMyImage") {
    dependsOn("buildMyImage")
    registryCredentials {
        url.set("https://hub.docker.com/v2/")
        username.set(dockerHubUsername)
        password.set(providers.gradleProperty("DOCKER_HUB_PASSWORD"))
        email.set("support@worldmandia.cc")
    }
    images.add("$imageName:$version")
}


application {
    mainClass.set("cc.worldmandia.MainKt")
}
