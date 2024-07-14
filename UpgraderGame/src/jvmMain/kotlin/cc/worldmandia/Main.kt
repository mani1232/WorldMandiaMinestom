package cc.worldmandia

import kotlinx.coroutines.coroutineScope

suspend fun main(): Unit = coroutineScope {
    val server = WMMinecraftServer()
    server.start()
    server.enableSkinsListener()
    server.enableAutoSave()
}