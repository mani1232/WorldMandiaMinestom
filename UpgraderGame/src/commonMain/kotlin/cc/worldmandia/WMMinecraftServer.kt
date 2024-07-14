package cc.worldmandia

import cc.worldmandia.chunk.WMChunkSupplier
import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import com.github.shynixn.mccoroutine.minestom.launch
import com.mayakapps.kache.InMemoryKache
import com.mayakapps.kache.KacheStrategy
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.*
import net.hollowcube.polar.PolarLoader
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.extras.lan.OpenToLAN
import net.minestom.server.world.DimensionType

class WMMinecraftServer {

  private val minecraftServer: MinecraftServer = MinecraftServer.init()

  private val globalEventHandler = MinecraftServer.getGlobalEventHandler()

  private val instanceManager = MinecraftServer.getInstanceManager()

  private val world = instanceManager.createInstanceContainer(DimensionType.OVERWORLD)

  private val skinsCache =
      InMemoryKache<String, PlayerSkin>(
          maxSize = MinecraftServer.getConnectionManager().onlinePlayers.size.toLong() + 1) {
            strategy = KacheStrategy.LRU
          }

  suspend fun start(address: String = "0.0.0.0", port: Int = 25565) = coroutineScope {
    OpenToLAN.open()

    // instanceContainer.setGenerator { unit -> unit.modifier().fillHeight(0, 40, Block.AIR) }

    world.chunkLoader = PolarLoader(Path("world/test"))
    world.chunkSupplier = WMChunkSupplier()

    globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
      val player = event.player
      event.spawningInstance = world
      player.gameMode = GameMode.CREATIVE
      player.respawnPoint = Pos(0.0, 75.0, 0.0)
    }

    println(
        "[MCCoroutineSampleServer/main] Is starting on Thread:${Thread.currentThread().name}/${Thread.currentThread().threadId()}")

    // Switches into suspendable scope on startup.
    minecraftServer.launch {
      println(
          "[MCCoroutineSampleServer/main] MainThread 1 Thread:${Thread.currentThread().name}/${Thread.currentThread().threadId()}")
      delay(2000)
      println(
          "[MCCoroutineSampleServer/main] MainThread 2 Thread:${Thread.currentThread().name}/${Thread.currentThread().threadId()}")

      withContext(Dispatchers.IO) {
        println(
            "[MCCoroutineSampleServer/main] Simulating data load Thread:${Thread.currentThread().name}/${Thread.currentThread().threadId()}")
        delay(500)
      }
      println(
          "[MCCoroutineSampleServer/main] MainThread 3 Thread:${Thread.currentThread().name}/${Thread.currentThread().threadId()}")
    }

    minecraftServer.start(address, port)
  }

  suspend fun enableAutoSave() = coroutineScope {
    launch {
      while (true) {
        delay(60.seconds)
        world.saveChunksToStorage()
      }
    }
  }

  fun enableSkinsListener() =
      globalEventHandler.addSuspendingListener(minecraftServer, PlayerSkinInitEvent::class.java) {
          event ->
        event.skin =
            skinsCache.getOrPut(event.player.username) {
              PlayerSkin.fromUsername(event.player.username)
            }
      }
}
