package net.quetzi.qutilities.world;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {

    private static Set<String> queue = new HashSet();

    public static boolean sendToSpawn(String playername) {

        if (MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(playername);
            ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(0).getSpawnPoint();
            if (player.getBedLocation(0) != null) {
                dest = player.getBedLocation(0);
                QUtilities.log.info("Player bed found for " + playername);
            } else {
                QUtilities.log.info("No bed found for " + playername + " moving to overworld spawn instead");
            }
            if (player.dimension != 0) {
                player.travelToDimension(0);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
            return true;
        } else {
            // Player is offline - process move on login
            if (!queue.contains(playername.toLowerCase())) {
                queue.add(playername.toLowerCase());
            }
        }
        return false;
    }

    public static void processQueue(String playername) {

        if (queue.size() > 0) {
            if (queue.contains(playername.toLowerCase())) {
                if (sendToSpawn(playername)) {
                    QUtilities.log.info("Player " + playername + " was queued to be moved, moving now.");
                    queue.remove(playername);
                }
            }
        }
    }
}
