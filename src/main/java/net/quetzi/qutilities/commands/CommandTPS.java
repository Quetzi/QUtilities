package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.helpers.SystemInfo;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandTPS extends CommandBase
{
    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");
    private List<String> aliases;

    public CommandTPS()
    {
        aliases = new ArrayList<>();
        aliases.add("qtps");
        aliases.add("tps");
    }

    private static long mean(long[] values)
    {
        long sum = 0l;
        for (long v : values)
        {
            sum += v;
        }
        return sum / values.length;
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "diminfo";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/diminfo";
    }

    @Nonnull
    @Override
    public List<String> getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        if (args.length == 0)
        {
            showTPSSummary(server, sender);
        }
        else
        {
            int dimension;
            try
            {
                dimension = parseInt(args[0]);
            }
            catch (NumberInvalidException e1)
            {
                sender.sendMessage(new TextComponentString("Invalid dimension ID."));
                return;
            }
            showTPSDetail(server, sender, dimension);
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            List<String> dimList = new ArrayList<>();
            for (World world : server.worlds)
            {
                dimList.add(String.valueOf(world.provider.getDimension()));
            }
            return dimList;
        }
        return new ArrayList<>();
    }

    private void showTPSSummary(MinecraftServer server, ICommandSender sender)
    {
        int chunksLoaded = 0;
        sender.sendMessage(new TextComponentString(SystemInfo.getUptime()));
        for (WorldServer world : server.worlds)
        {
            chunksLoaded += world.getChunkProvider().getLoadedChunkCount();
            sender.sendMessage(new TextComponentString("[" + world.provider.getDimension() + "]" + world.provider.getDimensionType().getName() + ": " + timeFormatter.format(SystemInfo.getWorldTickTime(world)) + "ms [" + timeFormatter.format(SystemInfo.getDimensionTPS(world))
                    + "]"));
        }
        sender.sendMessage(new TextComponentString("Total Chunks loaded: " + chunksLoaded));
        sender.sendMessage(new TextComponentString("Overall: " + timeFormatter.format(mean(server.tickTimeArray) * 1.0E-6D) + "ms ["
                + Math.min(1000.0 / (mean(server.tickTimeArray) * 1.0E-6D), 20) + "]"));
    }

    private void showTPSDetail(MinecraftServer server, ICommandSender sender, int dimension)
    {

        WorldServer world = server.worldServerForDimension(dimension);
        sender.sendMessage(new TextComponentString(SystemInfo.getUptime()));
        sender.sendMessage(new TextComponentString("Information for [" + dimension + "]" + world.provider.getDimensionType().getName()));
        sender.sendMessage(new TextComponentString("Players (" + world.playerEntities.size() + "): " + getPlayersForDimension(server, dimension)));
        sender.sendMessage(new TextComponentString("Item Entities: " + getItemEntityCount(world.loadedEntityList)));
        sender.sendMessage(new TextComponentString("Hostile Mobs: " + getHostileEntityCount(world.loadedEntityList)));
        sender.sendMessage(new TextComponentString("Passive Mobs: " + getPassiveEntityCount(world.loadedEntityList)));
        sender.sendMessage(new TextComponentString("Total Living Entities: " + getLivingEntityCount(world.loadedEntityList)));
        sender.sendMessage(new TextComponentString("Total Entities: " + world.loadedEntityList.size()));
        sender.sendMessage(new TextComponentString("Tile Entities: " + world.loadedTileEntityList.size()));
        sender.sendMessage(new TextComponentString("Loaded Chunks: " + world.getChunkProvider().getLoadedChunkCount()));
        sender.sendMessage(new TextComponentString("TPS: " + timeFormatter.format(SystemInfo.getWorldTickTime(world)) + "ms[" + SystemInfo.getDimensionTPS(world) + "]"));
    }

    private int getItemEntityCount(List list)
    {
        int count = 0;
        for (Object entity : list)
        {
            if (entity instanceof EntityItem)
            {
                count++;
            }
        }
        return count;
    }

    private int getPassiveEntityCount(List list)
    {
        int count = 0;
        for (Object entity : list)
        {
            if (entity instanceof EntityAnimal)
            {
                count++;
            }
        }
        return count;
    }

    private int getHostileEntityCount(List list)
    {
        int count = 0;
        for (Object entity : list)
        {
            if (entity instanceof EntityMob)
            {
                count++;
            }
        }
        return count;
    }

    private int getLivingEntityCount(List list)
    {
        int count = 0;
        for (Object entity : list)
        {
            if (entity instanceof EntityLiving)
            {
                count++;
            }
        }
        return count;
    }

    private String getPlayersForDimension(MinecraftServer server, int dimension)
    {
        ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) server.worldServerForDimension(dimension).playerEntities;
        if (players.size() == 0)
        {
            return "No players in this world";
        }
        else
        {
            String playersString = "";
            for (EntityPlayer player : players)
            {
                playersString = playersString + player.getName() + ", ";
            }
            return playersString.substring(0, playersString.lastIndexOf(playersString) - 2);
        }
    }
}
