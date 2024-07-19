package cc.worldmandia.chunk

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.Instance
import net.minestom.server.instance.LightingChunk
import net.minestom.server.utils.chunk.ChunkSupplier

class WMChunkSupplier : ChunkSupplier {
    override fun createChunk(instance: Instance, chunkX: Int, chunkZ: Int): Chunk {
        return LightingChunk(instance, chunkX, chunkZ)
    }
}
