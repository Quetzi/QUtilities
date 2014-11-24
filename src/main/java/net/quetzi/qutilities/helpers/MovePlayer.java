package net.quetzi.qutilities.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class MovePlayer {

    public static boolean sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);
            if (player.getBedLocation() != null) {
                return sendToBed(playername);
            } else {
                return sendToDimension(playername, 0);
            }
        }
        return false;
    }

    public static boolean sendToBed(String playername) {

        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);
        BlockPos dest = player.getBedLocation();
        return movePlayer(playername, 0, dest);
    }

    public static boolean sendToDimension(String playername, int dim) {

        BlockPos dest = MinecraftServer.getServer().worldServerForDimension(dim).getSpawnPoint();
        return movePlayer(playername, dim, dest);
    }

    public static boolean sendToLocation(String playername, int dim, int x, int y, int z) {

        return movePlayer(playername, dim, new BlockPos(x, y, z));
    }

    public static boolean movePlayer(String playername, int dim, BlockPos dest) {

        if (MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(playername);
            if (player.dimension != dim) {
                player.travelToDimension(dim);
            }
            player.setPositionAndUpdate(dest.getX(), dest.getY(), dest.getZ());
            return true;
        } else {
            queuePlayer(playername, dim, dest);
            return false;
        }
    }

    private static void queuePlayer(String playername, int dim, BlockPos dest) {

        if (!TeleportQueue.isQueued(playername.toLowerCase())) {
            TeleportQueue.add(playername.toLowerCase(), dim, dest.getX(), dest.getY(), dest.getZ());
        }
    }
}
