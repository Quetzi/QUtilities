package net.quetzi.qutilities.helpers;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.ChunkCoordIntPair;
import net.quetzi.qutilities.QUtilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quetzi on 16/12/14.
 */
public class ChunkTools {

    private static HashMap<WorldProvider, ChunkCoordIntPair> queue = new HashMap<WorldProvider, ChunkCoordIntPair>();
    public static boolean processQueue = false;

    public static boolean loadChunk(WorldProvider provider, int x, int z) {

        IChunkProvider chunkGenerator = provider.createChunkGenerator();
        if (!chunkGenerator.chunkExists(x, z)) {
            chunkGenerator.provideChunk(x, z);
            return true;
        }
        return false;
    }

    private static boolean queueChunk(WorldProvider provider, int x, int z) {

        ChunkCoordIntPair chunk = new ChunkCoordIntPair(x, z);
        IChunkProvider chunkGenerator = provider.createChunkGenerator();
        if (!chunkGenerator.chunkExists(x, z)) {
            queue.put(provider, chunk);
            return true;
        }
        return false;
    }
    public static boolean processQueue(WorldProvider provider) {

        for (Map.Entry<WorldProvider, ChunkCoordIntPair> entry : queue.entrySet()) {
            if (entry.getKey().equals(provider)) {
                if (loadChunk(entry.getKey(), entry.getValue().chunkXPos, entry.getValue().chunkZPos)) {
                    queue.remove(entry);
                    return true;
                }
            }
        }
        return false;
    }

    public static void generateChunks(WorldProvider provider, int startX, int startZ, int endX, int endZ) {

        queue.clear();
        if (startX < endX) {
            for (int i = startX; i <= endX; i++) {
                if (startZ < endZ) {
                    for (int j = startZ; j <= endZ; j++) {
                        queueChunk(provider, i, j);
                    }
                } else if (startZ > endZ) {
                    for (int j = startZ; j >= endZ; j--) {
                        queueChunk(provider, i, j);
                    }
                }
            }
        } else if (startX > endX) {
            for (int i = startX; i >= endX; i--) {
                if (startZ < endZ) {
                    for (int j = startZ; j <= endZ; j++) {
                        queueChunk(provider, i, j);
                    }
                } else if (startZ > endZ) {
                    for (int j = startZ; j >= endZ; j--) {
                        queueChunk(provider, i, j);
                    }
                }
            }
        }
        QUtilities.log.info("Queued " + queue.size() + " chunks for generation");
    }
}
