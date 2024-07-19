package cc.worldmandia.configuration.impl

import java.io.File

abstract class DefaultConfigsDirectory<T> {

    abstract val extensions: List<String>
    abstract val folder: File

    private val dirConfigFiles = mutableListOf<DefaultConfig<T>>()

    suspend fun updateAllFiles() {
        dirConfigFiles.forEach { it.updateFile(it.data!!) }
    }

    abstract suspend fun loadDefaultFiles(files: Map<String, T>)
}
