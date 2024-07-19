package cc.worldmandia.configuration.files

import cc.worldmandia.configuration.ConfigUtils
import cc.worldmandia.configuration.files.data.ConfigData
import cc.worldmandia.configuration.files.data.WorldData
import cc.worldmandia.configuration.impl.DefaultConfig

object ConfigFiles {

    lateinit var config: DefaultConfig<ConfigData>
    lateinit var worlds: DefaultConfig<WorldData>

    suspend fun init() {
        config = ConfigUtils.createYamlConfig("config", ConfigData())
        config.loadDefaultFile()
        worlds = ConfigUtils.createYamlConfig("worlds", WorldData())
        worlds.loadDefaultFile()
    }

}