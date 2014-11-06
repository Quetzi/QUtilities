package net.quetzi.qutilities.helpers;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {

    public static void sendToDefaultSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.getBedLocation(0) != null) {
                sendToBed(playername);
                QUtilities.log.info("Player bed found for " + playername);
            } else {
                sendToDimension(playername, 0);
                QUtilities.log.info("No bed found for " + playername + " moving to overworld spawn instead");
            }
        }
    }

    public static void sendToBed(String playername) {

        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
        ChunkCoordinates dest = player.getBedLocation(0);
        movePlayer(playername, 0, dest);
    }

    public static void sendToDimension(String playername, int dim) {

        ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(dim).getSpawnPoint();
        movePlayer(playername, dim, dest);
    }

    public static void sendToLocation(String playername, int dim, int x, int y, int z) {

        movePlayer(playername, dim, new ChunkCoordinates(x, y, z));
    }

    public static void movePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            if (player.dimension != dim) {
                player.travelToDimension(dim);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
        } else {
            queuePlayer(playername, dim, dest);
        }
    }

    private static void queuePlayer(String playername, int dim, ChunkCoordinates dest) {

        if (!TeleportQueue.isQueued(playername.toLowerCase())) {
            TeleportQueue.add(playername.toLowerCase(), dim, dest.posX, dest.posY, dest.posZ);
        }
    }
}
