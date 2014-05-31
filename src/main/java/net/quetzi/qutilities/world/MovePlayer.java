package net.quetzi.qutilities.world;

import java.util.List;

import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer {
    public static List<String> queue;

    public static boolean sendToSpawn(String playername) {
        if (MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(playername) != null) {
            EntityPlayer player = MinecraftServer.getServer().getConfigurationManager()
                    .getPlayerForUsername(playername);
            ChunkCoordinates dest = MinecraftServer.getServer().worldServerForDimension(0)
                    .getSpawnPoint();
            if (player.getBedLocation(0) != null) {
                dest = player.getBedLocation(0);
                QUtilities.qLog.info("Player bed found for " + playername);
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

    public static void processQueue(String name) {
        if (queue.size() > 0) {
            for (int i = 0; i < queue.size(); i++) {
                if (queue.get(i).toLowerCase().matches(name.toLowerCase())) {
                    if (sendToSpawn(queue.get(i))) {
                        QUtilities.qLog.info("Player " + queue.get(i)
                                + " was queued to be moved, moving now.");
                        queue.remove(i);
                    }
                }
            }
        }
    }
}
