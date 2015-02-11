package net.quetzi.qutilities.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {

    public static boolean sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.getBedLocation(0) != null) {
                return sendToBed(playername);
            } else {
                return sendToDimension(playername, 0);
            }
        }
        return false;
    }

    public static boolean sendToBed(String playername) {

        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
        ChunkCoordinates dest = player.getBedLocation(0);
        return movePlayer(playername, 0, dest);
    }

    public static boolean sendToDimension(String playername, int dim) {

        ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(dim).getSpawnPoint();
        return movePlayer(playername, dim, dest);
    }

    public static boolean sendToLocation(String playername, int dim, int x, int y, int z) {

        return movePlayer(playername, dim, new ChunkCoordinates(x, y, z));
    }

    public static boolean movePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.dimension != dim) {
                player.travelToDimension(dim);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
            return true;
        } else {
            queuePlayer(playername, dim, dest);
            return false;
        }
    }

    private static void queuePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (!QUtilities.queue.isQueued(playername.toLowerCase())) {
            QUtilities.queue.add(playername.toLowerCase(), dim, dest.posX, dest.posY, dest.posZ);
        }
    }
}
