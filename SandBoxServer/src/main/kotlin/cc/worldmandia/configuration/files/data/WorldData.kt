package cc.worldmandia.configuration.files.data

import kotlinx.serialization.Serializable

@Serializable
data class WorldData(
    val worlds: MutableList<String> = mutableListOf(),
)
