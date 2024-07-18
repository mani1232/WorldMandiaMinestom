package cc.worldmandia.configuration

import cc.worldmandia.configuration.data.ConfigData
import cc.worldmandia.configuration.data.WorldData
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toOkioPath
import kotlin.io.path.Path

class ConfigFile {

    private val configPath = Path("config.json").toOkioPath()
    private val worldPath = Path("world.json").toOkioPath()

    init {
        config = storeOf(configPath, ConfigData())
        world = storeOf(worldPath, WorldData())
    }

    suspend fun update() {
        if (!configPath.toFile().exists()) {
            config.reset()
        }
        if (!worldPath.toFile().exists()) {
            world.reset()
        }
    }

    companion object {
        lateinit var config: KStore<ConfigData>
        lateinit var world: KStore<WorldData>
    }
}
