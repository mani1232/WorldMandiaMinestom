package cc.worldmandia.configuration.files.data

import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    val address: String = "0.0.0.0",
    val port: Int = 25400,
    val maxOnline: Int = 100,
    val openToLan: Boolean = false,
    val mojangAuth: Boolean = false,
    val brandName: String = "WorldMandia",
    val favicon: String = "logo.png",
    val velocityProxy: Velocity = Velocity(),
    val advancedSettings: AdvancedSettings = AdvancedSettings(),
) {
    @Serializable
    data class Velocity(
        val enabled: Boolean = false,
        val secretKey: String = "",
    )

    @Serializable
    data class AdvancedSettings(
        val skinsCacheHistorySize: Long = 50,
    )
}
