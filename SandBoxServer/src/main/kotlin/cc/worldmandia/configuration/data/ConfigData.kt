package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val address: String = "0.0.0.0",
    val port: Int = 25400,
    val openToLan: Boolean = false,
    val mojangAuth: Boolean = false,
    val velocityProxy: Velocity = Velocity(),
) {
    @Serializable
    data class Velocity(
        val enabled: Boolean = false,
        val secretKey: String = "",
    )
}
