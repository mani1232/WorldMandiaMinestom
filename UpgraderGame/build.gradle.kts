plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.ktor)
}

group = "cc.worldmandia"

version = "1.0-SNAPSHOT"

kotlin {
  jvm()

  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.kotlinx.coroutines)
        implementation(libs.kotlinx.serialization.json)
        implementation(libs.other.klogging.slf4j)
        implementation(libs.other.cache)
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
    }
    jvmMain { dependencies {} }
  }
}

application { mainClass.set("cc.worldmandia.MainKt") }
