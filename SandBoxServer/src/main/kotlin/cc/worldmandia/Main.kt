package cc.worldmandia

import cc.worldmandia.configuration.data.ConfigFiles
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    ConfigFiles.init()
    val server = WMMinecraftServer(
        address = ConfigFiles.config.data.address,
        port = ConfigFiles.config.data.port
    )
    server.start()
    server.enableSkinsListener()
    server.enableAutoSave()
}