package cc.worldmandia

import cc.worldmandia.chunk.WMChunkSupplier
import cc.worldmandia.configuration.files.ConfigFiles
import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import com.github.shynixn.mccoroutine.minestom.launch
import com.mayakapps.kache.InMemoryKache
import com.mayakapps.kache.KacheStrategy
import io.klogging.logger
import kotlinx.coroutines.*
import net.hollowcube.polar.PolarLoader
import net.hollowcube.polar.PolarWorld
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerSkinInitEvent
import net.minestom.server.event.server.ServerListPingEvent
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.lan.OpenToLAN
import net.minestom.server.extras.velocity.VelocityProxy
import net.minestom.server.instance.block.Block
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biome.Biome
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.Path
import kotlin.time.Duration.Companion.seconds


class WMMinecraftServer(private val address: String, private val port: Int) {

    private val minecraftServer: MinecraftServer = MinecraftServer.init()

    private val globalEventHandler = MinecraftServer.getGlobalEventHandler()

    private val instanceManager = MinecraftServer.getInstanceManager()

    private val connectionManager = MinecraftServer.getConnectionManager()

    private val world = instanceManager.createInstanceContainer(DimensionType.OVERWORLD)

    private val skinsCache =
        InMemoryKache<String, PlayerSkin>(
            maxSize = ConfigFiles.config.data.maxOnline + ConfigFiles.config.data.advancedSettings.skinsCacheHistorySize
        ) {
            strategy = KacheStrategy.LRU
        }

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun start() = coroutineScope {
        LOGGER.info { "Starting Minecraft server on $address:$port" }
        MinecraftServer.setBrandName(ConfigFiles.config.data.brandName)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run(): Unit = runBlocking {
                withContext(Dispatchers.IO) {
                    LOGGER.info("Shutting down...")
                    world.saveChunksToStorage()
                    LOGGER.info("Worlds saved successfully!")
                    MinecraftServer.stopCleanly()
                    LOGGER.info("Shutting down is DONE!")
                }
            }
        })

        globalEventHandler.addListener(ServerListPingEvent::class.java) {
            val data = it.responseData
            data.maxPlayer = ConfigFiles.config.data.maxOnline
            data.entries.addAll(connectionManager.onlinePlayers)
            data.description = Component.text("My server", NamedTextColor.RED)
            data.favicon = "data:image/png;base64,${Base64.encode(File(ConfigFiles.config.data.favicon).readBytes())}"
        }

        minecraftServer.launch {

        }

        if (ConfigFiles.config.data.openToLan) OpenToLAN.open()

        if (ConfigFiles.config.data.mojangAuth) MojangAuth.init()

        if (ConfigFiles.config.data.velocityProxy.enabled) VelocityProxy.enable(ConfigFiles.config.data.velocityProxy.secretKey)

        val worldsDirPath = Path("worlds/")
        worldsDirPath.toFile().mkdirs()

        world.setGenerator { unit ->
            unit.modifier().apply { this.fillBiome(Biome.BAMBOO_JUNGLE) }
                .apply { this.fillHeight(0, 40, Block.GRASS_BLOCK) }
        }
        val loader = PolarLoader(worldsDirPath.resolve("test.polar")).setParallel(true)
        loader.world().setCompression(PolarWorld.CompressionType.LZ4_FAST)
        world.chunkLoader = loader
        world.chunkSupplier = WMChunkSupplier()
        world.saveChunksToStorage()

        globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
            val player = event.player
            event.spawningInstance = world
            player.gameMode = GameMode.CREATIVE
            player.respawnPoint = Pos(0.0, 41.0, 0.0)
        }

        minecraftServer.start(address, port)
        LOGGER.info("Server started!")
    }

    suspend fun enableAutoSave() = withContext(Dispatchers.IO) {
        launch {
            val log = logger("Auto-Save-World")
            val autoSaveTime = 60
            log.info { "Enabled auto save worlds every $autoSaveTime seconds" }
            while (true) {
                delay(autoSaveTime.seconds)
                log.debug { "World saved" }
                world.saveChunksToStorage()
            }
        }
    }

    fun enableSkinsListener() =
        globalEventHandler.addSuspendingListener(minecraftServer, PlayerSkinInitEvent::class.java) { event ->
            event.skin =
                skinsCache.getOrPut(event.player.username) {
                    PlayerSkin.fromUsername(event.player.username)
                }
        }
}
