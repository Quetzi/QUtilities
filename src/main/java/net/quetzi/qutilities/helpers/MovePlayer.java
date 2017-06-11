package net.quetzi.qutilities.helpers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.quetzi.qutilities.QUtilities;

public class MovePlayer
{
    public static boolean sendToDefaultSpawn(String playername)
    {
        if (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playername) != null)
        {
            EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playername);
            if (player != null)
            {
                if (player.getBedLocation(0) != null)
                {
                    return movePlayer(playername, 0, player.getBedLocation(0));
                }
                else
                {
                    return sendToDimension(playername, 0);
                }
            }
        }
        QUtilities.queue.addToQueue(playername);
        return false;
    }

    public static boolean sendToDimension(String playername, int dim)
    {
        BlockPos dest = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim).getSpawnPoint();
        return movePlayer(playername, dim, dest);
    }

    public static boolean sendToLocation(String playername, int dim, int x, int y, int z)
    {
        return sendToLocation(playername, dim, (double) x, (double) y, (double) z);
    }

    public static boolean sendToLocation(String playername, int dim, double x, double y, double z)
    {
        return movePlayer(playername, dim, x, y, z);
    }

    public static boolean movePlayer(String playername, int dim, BlockPos dest)
    {
        return movePlayer(playername, dim, dest.getX(), dest.getY(), dest.getZ());
    }

    public static boolean movePlayer(String playername, int dim, double x, double y, double z)
    {
        EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playername);

        if (player != null)
        {
            if (player.dimension != dim)
            {
                FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().transferPlayerToDimension(player, dim, new Teleporter(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim)));
            }
            player.setPositionAndUpdate(x, y, z);
            return true;
        }
        else
        {
            queuePlayer(playername, dim, x, y, z);
            return false;
        }
    }

    private static boolean queuePlayer(String playername, int dim, double x, double y, double z)
    {
        return !QUtilities.queue.isQueued(playername) && QUtilities.queue.addToQueue(playername, dim, x, y, z);
    }
}