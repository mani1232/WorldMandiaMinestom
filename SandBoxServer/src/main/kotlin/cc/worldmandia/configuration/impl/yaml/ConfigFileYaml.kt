package cc.worldmandia.configuration.impl.yaml

import cc.worldmandia.LOGGER
import com.charleskorn.kaml.*
import cc.worldmandia.configuration.impl.DefaultConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import java.io.File
import java.nio.file.Path

class ConfigFileYaml<T>(
    override val file: File,
    module: SerializersModule,
    private val serializer: KSerializer<T>,
    override val extensions: List<String>,
    override var data: T
) : DefaultConfig<T>() {

    companion object {
        inline fun <reified T> create(path: Path, fileName: String, module: SerializersModule, defaultData: T) =
            ConfigFileYaml(
                File(path.resolve(fileName).toUri()), module, module.serializer<T>(), listOf("yml", "yaml"), defaultData
            )
    }

    private val yaml =
        Yaml(
            serializersModule = module,
            configuration =
            YamlConfiguration(
                encodeDefaults = true,
                strictMode = false,
                polymorphismStyle = PolymorphismStyle.Tag,
                allowAnchorsAndAliases = true
            )
        )

    override suspend fun updateFile(dataClass: T): T? {
        data =
            try {
                yaml.encodeToStream(serializer, dataClass, file.outputStream())
                dataClass
            } catch (e: SerializationException) {
                LOGGER.error("""Error in file: ${file.name}|${e.message}""".trimMargin())
                return null
            }

        return data
    }

    override suspend fun loadFile(): T? {
        data =
            try {
                yaml.decodeFromStream(serializer, file.inputStream())
            } catch (e: SerializationException) {
                LOGGER.error("""Error in file: ${file.name}|${e.message}""".trimMargin())
                return null
            }
        return data
    }
}
