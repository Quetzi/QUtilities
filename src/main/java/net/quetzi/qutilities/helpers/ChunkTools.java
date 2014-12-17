package net.quetzi.qutilities.helpers;

import net.minecraft.util.BlockPos;
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

    public static void queueRect(WorldProvider provider, int startX, int startZ, int endX, int endZ) {

        QUtilities.log.info("Clearing chunk generation queue");
        queue.clear();

        ChunkCoordIntPair centre = getCentreChunk(new ChunkCoordIntPair(startX, startZ), new ChunkCoordIntPair(endX, endZ));
        int runCount = getIterations(startX, startZ, endX, endZ);
        for (int distance = 0; distance <= runCount; distance++) {
            for (int i = centre.chunkZPos; i <= startZ; i++) {
                queueChunk(provider, centre.chunkXPos - distance, i);
                queueChunk(provider, centre.chunkXPos + distance, i);
            }
            for (int j = centre.chunkZPos; j <= startZ; j++) {
                queueChunk(provider, j, centre.chunkZPos - distance);
                queueChunk(provider, j, centre.chunkZPos + distance);
            }
        }
        QUtilities.log.info("Queued " + queue.size() + " chunks for generation");
    }

    public static void queueSquare(WorldProvider provider, int x, int z, int size) {

        QUtilities.log.info("Clearing chunk generation queue");
        queue.clear();

        for (int distance = 0; distance <= size; distance++) {
            for (int i = x - distance; i <= (x + distance); i++) {
                queueChunk(provider, i, z - distance);
                queueChunk(provider, i, z + distance);
            }
            for (int j = z - distance; j <= (z + distance); j++) {
                queueChunk(provider, x - distance, j);
                queueChunk(provider, x + distance, j);
            }
        }
        QUtilities.log.info("Queued " + queue.size() + " chunks for generation");
    }

    public static int getIterations(int startX, int startZ, int endX, int endZ) {

        return (startX - endX > startZ - endZ) ? startX- endX / 2 : startZ - endZ / 2;
    }

    public static ChunkCoordIntPair getCentreChunk(ChunkCoordIntPair chunk1, ChunkCoordIntPair chunk2) {

        int x = chunk1.chunkXPos - chunk2.chunkXPos / 2;
        int z = chunk1.chunkZPos - chunk2.chunkZPos / 2;
        return new ChunkCoordIntPair(chunk1.chunkXPos - x, chunk1.chunkZPos - z);
    }

    public static ChunkCoordIntPair getChunkCoordsFromBlock(BlockPos blockPos) {

        return new ChunkCoordIntPair(blockPos.getX() / 16, blockPos.getZ() / 16);
    }

    public static int getQueueSize() {
        
        return queue.size();
    }
}
