package cc.worldmandia.configuration

import io.github.xxfast.kstore.file.storeOf
import kotlinx.io.files.Path

object ConfigFile {
    val config = storeOf(Path("config.json"), 1)

    val world = storeOf(Path("worlds.json"), 1)
}
