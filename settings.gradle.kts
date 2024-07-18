pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven("https://jitpack.io")
        mavenCentral()
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.worldmandia.cc/snapshots")
        maven("https://repo.worldmandia.cc/releases")
    }
}

rootProject.name = "WorldMandiaMinestom"
include("UpgraderGame")
