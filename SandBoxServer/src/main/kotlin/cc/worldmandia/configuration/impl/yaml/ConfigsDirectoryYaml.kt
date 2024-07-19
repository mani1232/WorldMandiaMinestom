package cc.worldmandia.configuration.impl.yaml

import cc.worldmandia.configuration.impl.DefaultConfigsDirectory
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import java.io.File

class ConfigsDirectoryYaml<T>(
    override val folder: File,
    private val module: SerializersModule,
    private val serializer: KSerializer<T>,
    override val extensions: List<String>,
) : DefaultConfigsDirectory<T>() {

    private val dirConfigFiles = mutableListOf<ConfigFileYaml<T>>()

    companion object {
        inline fun <reified T> create(file: File, module: SerializersModule) =
            ConfigsDirectoryYaml(file, module, module.serializer<T>(), listOf("yml", "yaml"))
    }

    override suspend fun loadDefaultFiles(files: Map<String, T>) {
        dirConfigFiles.addAll(
            files.map { (t, u) ->
                ConfigFileYaml(folder.resolve(t), module, serializer, extensions, u)
            })
        if (!folder.exists()) {
            folder.mkdirs()
            dirConfigFiles.forEach { it.loadDefaultFile() }
        } else {
            dirConfigFiles.forEach { it.loadFile() }
        }
    }
}
