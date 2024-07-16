plugins {
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.ktor) apply false
    alias(libs.plugins.docker) apply false
    alias(libs.plugins.docker.java) apply false
    alias(libs.plugins.updateDeps) apply true
}

group = "cc.worldmandia"
version = "1.0-SNAPSHOT"