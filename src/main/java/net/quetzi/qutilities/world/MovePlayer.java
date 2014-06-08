package net.quetzi.qutilities.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

import java.util.HashSet;
import java.util.Set;

public class MovePlayer
{
    public static Set<String> queue = new HashSet();

    public static boolean sendToSpawn(String playername)
    {
        if (MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playername);
            ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(0).getSpawnPoint();
            if (player.getBedLocation(0) != null) {
                dest = player.getBedLocation(0);
                QUtilities.qLog.info("Player bed found for " + playername);
            } else {
                QUtilities.qLog.info("No bed found for " + playername + " moving to overworld spawn instead");
            }
            if (player.dimension != 0) {
                player.travelToDimension(0);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
            return true;
        } else {
            // Player is offline - process move on login
            queue.add(playername);
        }
        return false;
    }

    public static void processQueue(String playername)
    {
        if (queue.size() > 0) {
            if (queue.contains(playername.toLowerCase())) {
                if (sendToSpawn(playername)) {
                    QUtilities.qLog.info("Player " + playername + " was queued to be moved, moving now.");
                    queue.remove(playername);
                }
            }
        }
    }
}
