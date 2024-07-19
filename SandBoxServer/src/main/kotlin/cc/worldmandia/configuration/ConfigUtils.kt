package cc.worldmandia.configuration

import cc.worldmandia.configuration.impl.DefaultConfig
import cc.worldmandia.configuration.impl.json.ConfigFileJson
import cc.worldmandia.configuration.impl.yaml.ConfigFileYaml
import kotlinx.serialization.modules.SerializersModule
import java.nio.file.Path

object ConfigUtils {

    var defaultPath: Path = Path.of("")

    inline fun <reified T> createYamlConfig(configName: String, defaultConfig: T): DefaultConfig<T> {
        return ConfigFileYaml.create<T>(defaultPath, "$configName.yml", SerializersModule {}, defaultConfig)
    }

    inline fun <reified T> createJsonConfig(configName: String, defaultConfig: T): DefaultConfig<T> {
        return ConfigFileJson.create<T>(defaultPath, "$configName.json", SerializersModule {}, defaultConfig)
    }
}
