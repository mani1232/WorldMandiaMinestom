package cc.worldmandia

import cc.worldmandia.configuration.files.ConfigFiles
import io.klogging.logger
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

val LOGGER = logger("General")

fun main(): Unit = runBlocking {
    val time = measureExecutionTime {
        ConfigFiles.init()
        val server = WMMinecraftServer(
            address = ConfigFiles.config.data.address,
            port = ConfigFiles.config.data.port
        )
        server.start()
        server.enableSkinsListener()
        server.enableAutoSave()
    }

    LOGGER.info { "Done! (${time}ms)" }
}

suspend fun measureExecutionTime(block: suspend () -> Unit): Long {
    return measureTimeMillis {
        block()
    }
}