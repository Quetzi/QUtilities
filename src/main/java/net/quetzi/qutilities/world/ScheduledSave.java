package net.quetzi.qutilities.world;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ScheduledSave {
    @SubscribeEvent
    public void saveWorldState(WorldTickEvent event) {
        // 1200 = every 1 minute
        if ((event.phase == TickEvent.Phase.END) && (event.world.provider.dimensionId == 0) && (event.world.getWorldTime() % 1200 == 0)) {
            MinecraftServer server = MinecraftServer.getServer();
            if (server.getConfigurationManager() != null) {
                server.getConfigurationManager().saveAllPlayerData();
            }

            try {
                int i;
                WorldServer worldserver;
                boolean flag;

                for (i = 0; i < server.worldServers.length; ++i) {
                    if (server.worldServers[i] != null) {
                        worldserver = server.worldServers[i];
                        flag = worldserver.levelSaving;
                        worldserver.levelSaving = false;
                        worldserver.saveAllChunks(true, (IProgressUpdate) null);
                        worldserver.levelSaving = flag;
                    }
                }
            } catch (MinecraftException minecraftexception) {
                QUtilities.qLog.info("Failed to save the world!");
                return;
            }

            QUtilities.qLog.info("The world has been saved");
        }
    }
}
