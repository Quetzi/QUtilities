package net.quetzi.qutilities.helpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;

public class ScheduledSave {

    public static void saveWorldState() {

        MinecraftServer server = MinecraftServer.getServer();
        if (server.getConfigurationManager() != null) {
            server.getConfigurationManager().saveAllPlayerData();
        }
        try {
            int i;
            WorldServer worldserver;
//            boolean flag;
            for (i = 0; i < server.worldServers.length; ++i) {
                if (server.worldServers[i] != null) {
                    worldserver = server.worldServers[i];
                    // flag = worldserver.levelSaving;
                    worldserver.levelSaving = false;
                    worldserver.saveAllChunks(true, null);
                    // worldserver.levelSaving = flag;
                }
            }
        } catch (MinecraftException minecraftexception) {
            QUtilities.log.info("Failed to save the world!");
            return;
        }
        QUtilities.log.info("The world has been saved");
    }
}
