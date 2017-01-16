package net.quetzi.qutilities.helpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.qutilities.QUtilities;

public class ScheduledSave
{
    public static void saveWorldState()
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance().getServer();
        if (server != null)
        {
            server.getPlayerList().saveAllPlayerData();
            try
            {
                int i;
                WorldServer worldserver;
                for (i = 0; i < server.worlds.length; ++i)
                {
                    if (server.worlds[i] != null)
                    {
                        worldserver = server.worlds[i];
                        worldserver.saveAllChunks(true, null);
                    }
                }
            } catch (MinecraftException minecraftexception)
            {
                QUtilities.log.info("Failed to save the world!");
                return;
            }
        }
        QUtilities.log.info("The world has been saved");
    }
}
