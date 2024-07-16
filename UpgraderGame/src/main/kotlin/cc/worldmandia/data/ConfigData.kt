package cc.worldmandia.data

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val address: String = "0.0.0.0",
    val port: Int = 25565,
)
