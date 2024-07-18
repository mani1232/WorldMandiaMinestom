package cc.worldmandia

import cc.worldmandia.configuration.ConfigFile
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    ConfigFile()
    val server = WMMinecraftServer(address = ConfigFile.config.get()?.address ?: "localhost", port = ConfigFile.config.get()?.port ?: 25400)
    server.start()
    server.enableSkinsListener()
    server.enableAutoSave()
}