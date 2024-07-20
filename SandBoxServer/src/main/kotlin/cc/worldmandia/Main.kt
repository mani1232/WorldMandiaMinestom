package cc.worldmandia

import cc.worldmandia.configuration.files.ConfigFiles
import io.klogging.logger
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

val LOGGER = logger("General")

suspend fun main(): Unit = LOGGER.info {
    "Done! (${
        measureTimeMillis {
            runBlocking { 
                launch {
                    ConfigFiles.init()
                    val server = WMMinecraftServer(
                        address = ConfigFiles.config.data.address,
                        port = ConfigFiles.config.data.port
                    )
                    server.start()
                    server.enableSkinsListener()
                    server.enableAutoSave()
                }
            }
        }
    }ms)"
}