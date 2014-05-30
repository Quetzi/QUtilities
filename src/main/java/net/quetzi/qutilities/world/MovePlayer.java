package net.quetzi.qutilities.world;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;

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
            }
            if (player.dimension != 0) {
                player.travelToDimension(0);
            }
            player.setPositionAndUpdate(dest.posX, dest.posY, dest.posZ);
        } else {
            // Player is offline - process move on login
            queue.add(playername);
        }
        return false;
    }
}
