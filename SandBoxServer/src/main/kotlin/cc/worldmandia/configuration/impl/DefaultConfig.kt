package cc.worldmandia.configuration.impl

import cc.worldmandia.LOGGER
import cc.worldmandia.configuration.IConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import java.io.File

abstract class DefaultConfig<T> : IConfig {

    abstract val extensions: List<String>

    abstract val file: File

    abstract var data: T

    abstract suspend fun updateFile(dataClass: T): T?

    suspend fun loadDefaultFile(): T? = withContext(Dispatchers.IO) {
        try {
            val newData = if (file.createNewFile()) {
                updateFile(data)
            } else {
                loadFile()
            }
            if (newData != null) {
                data = newData
            }
        } catch (e: SerializationException) {
            LOGGER.error("""Error in file: ${file.name}|${e.message}""".trimMargin())
            return@withContext null
        }
        return@withContext data
    }

    abstract suspend fun loadFile(): T?
}
