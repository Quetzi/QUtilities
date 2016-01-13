package net.quetzi.qutilities.helpers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {

    public static boolean sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername) != null) {
            EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);
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

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);
        BlockPos dest = player.getBedLocation(0);
        return movePlayer(playername, 0, dest);
    }

    public static boolean sendToDimension(String playername, int dim) {

        BlockPos dest = MinecraftServer.getServer().worldServerForDimension(dim).getSpawnPoint();
        return movePlayer(playername, dim, dest);
    }

    public static boolean sendToLocation(String playername, int dim, int x, int y, int z) {

        return sendToLocation(playername, dim, (double)x, (double)y, (double)z);
    }

    public static boolean sendToLocation(String playername, int dim, double x, double y, double z) {

        return movePlayer(playername, dim, x, y, z);
    }

    public static boolean movePlayer(String playername, int dim, BlockPos dest) {
        return movePlayer(playername, dim, dest.getX(), dest.getY(), dest.getZ());
    }
    public static boolean movePlayer(String playername, int dim, double x, double y, double z) {

        EntityPlayerMP player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);

        if (MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername) != null) {
            if (player.dimension != dim) {
                MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dim);
            }
            player.setPositionAndUpdate(x, y, z);
            return true;
        } else {
            queuePlayer(playername, dim, x, y, z);
            return false;
        }
    }

    private static boolean queuePlayer(String playername, int dim, double x, double y, double z) {

        if (!QUtilities.queue.isQueued(playername)) {
            return QUtilities.queue.addToQueue(playername, dim, x, y, z);
        }
        return false;
    }
}