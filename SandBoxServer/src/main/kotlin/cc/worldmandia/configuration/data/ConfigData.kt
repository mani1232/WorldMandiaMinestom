package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val address: String = "0.0.0.0",
    val port: Int = 25400,
)
