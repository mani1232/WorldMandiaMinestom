package cc.worldmandia.configuration.data

import kotlinx.serialization.Serializable

@Serializable
data class WorldData(
    val worlds: MutableList<String> = mutableListOf(),
)
