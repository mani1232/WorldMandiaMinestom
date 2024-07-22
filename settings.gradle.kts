pluginManagement {
    repositories {
        //mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.5.0")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven("https://repo.worldmandia.cc/snapshots")
        maven("https://repo.worldmandia.cc/releases")
        maven("https://jitpack.io")
        mavenCentral()
    }
}

rootProject.name = "WorldMandiaMinestom"
include("SandBoxServer")
