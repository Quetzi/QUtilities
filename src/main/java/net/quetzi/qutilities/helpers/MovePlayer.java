package net.quetzi.qutilities.helpers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {

    public static boolean sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.getBedLocation(0) != null) {
                return sendToBed(playername);
            } else {
                return sendToDimension(playername, 0);
            }
        }
        QUtilities.queue.addToQueue(playername);
        return false;
    }

    public static boolean sendToBed(String playername) {

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
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

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            if (player.dimension != dim) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dim));
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
            return true;
        } else {
            queuePlayer(playername, dim, dest);
            return false;
        }
    }

    private static boolean queuePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (!QUtilities.queue.isQueued(playername)) {
            return QUtilities.queue.addToQueue(playername, dim, dest.posX, dest.posY, dest.posZ);
        }
        return false;
    }
}
