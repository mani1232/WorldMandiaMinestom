package cc.worldmandia

import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val server = WMMinecraftServer()
    server.start()
    server.enableSkinsListener()
    server.enableAutoSave()
}