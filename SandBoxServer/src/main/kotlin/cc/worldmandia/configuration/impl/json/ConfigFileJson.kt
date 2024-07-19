package cc.worldmandia.configuration.impl.json

import cc.worldmandia.LOGGER
import cc.worldmandia.configuration.impl.DefaultConfig
import cc.worldmandia.configuration.impl.yaml.ConfigFileYaml
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import java.io.File
import java.nio.file.Path

class ConfigFileJson<T>(
    module: SerializersModule,
    private val serializer: KSerializer<T>,
    override val extensions: List<String>,
    override val file: File,
    override var data: T
) : DefaultConfig<T>() {

    companion object {
        inline fun <reified T> create(path: Path, fileName: String, module: SerializersModule, defaultData: T) =
            ConfigFileYaml(
                File(path.resolve(fileName).toUri()), module, module.serializer<T>(), listOf("json"), defaultData
            )
    }

    private val json = Json {
        serializersModule = module
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun updateFile(dataClass: T): T? {
        data =
            try {
                json.encodeToStream(serializer, dataClass, file.outputStream())
                dataClass
            } catch (e: SerializationException) {
                LOGGER.error("""Error in file: ${file.name}|${e.message}""".trimMargin())
                return null
            }
        return data
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun loadFile(): T? {
        data =
            try {
                json.decodeFromStream(serializer, file.inputStream())
            } catch (e: SerializationException) {
                LOGGER.error("""Error in file: ${file.name}|${e.message}""".trimMargin())
                return null
            }
        return data
    }
}
