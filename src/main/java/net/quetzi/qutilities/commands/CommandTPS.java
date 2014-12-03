package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.helpers.SystemInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandTPS extends CommandBase {

    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");
    List<String>                       aliases;
    WorldServer                        world;

    public CommandTPS() {

        aliases = new ArrayList<String>();
        aliases.add("qtps");
        aliases.add("tps");
    }

    private static long mean(long[] values) {

        long sum = 0l;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }

    @Override
    public String getName() {

        return "diminfo";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/diminfo";
    }

    @Override
    public List getAliases() {

        return aliases;
    }

    @Override
    public void execute(ICommandSender icommandsender, String[] args) {

        if (args.length == 0) {
            showTPSSummary(icommandsender);
        } else {
            int dimension;
            try {
                dimension = NumberFormat.getInstance().parse(args[0]).intValue();
            } catch (ParseException e1) {
                icommandsender.addChatMessage(new ChatComponentText("Invalid dimension ID."));
                return;
            }
            showTPSDetail(icommandsender, dimension);
        }
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring, BlockPos pos) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    private void showTPSSummary(ICommandSender sender) {

        int chunksLoaded = 0;
        sender.addChatMessage(new ChatComponentText(SystemInfo.getUptime()));
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            double worldTickLength = SystemInfo.getWorldTickTime(world);
            double worldTPS = SystemInfo.getDimensionTPS(world);
            chunksLoaded += world.getChunkProvider().getLoadedChunkCount();
            sender.addChatMessage(new ChatComponentText("[" + world.provider.getDimensionId() + "]" + world.provider.getDimensionName() + ": " + timeFormatter.format(worldTickLength) + "ms [" + timeFormatter.format(worldTPS)
                    + "]"));
        }
        sender.addChatMessage(new ChatComponentText("Total Chunks loaded: " + chunksLoaded));
        sender.addChatMessage(new ChatComponentText("Overall: " + timeFormatter.format(mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D) + "ms ["
                + Math.min(1000.0 / (mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D), 20) + "]"));
    }

    private void showTPSDetail(ICommandSender sender, int dimension) {

        MinecraftServer server = MinecraftServer.getServer();
        double worldTickTime = mean(server.worldTickTimes.get(dimension)) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);
        this.world = MinecraftServer.getServer().worldServerForDimension(dimension);
        sender.addChatMessage(new ChatComponentText(SystemInfo.getUptime()));
        sender.addChatMessage(new ChatComponentText("Information for [" + dimension + "]" + world.provider.getDimensionName()));
        sender.addChatMessage(new ChatComponentText("Players (" + world.playerEntities.size() + "): " + getPlayersForDimension(dimension)));
        sender.addChatMessage(new ChatComponentText("Item Entities: " + getItemEntityCount(world.loadedEntityList)));
        sender.addChatMessage(new ChatComponentText("Hostile Mobs: " + getHostileEntityCount(world.loadedEntityList)));
        sender.addChatMessage(new ChatComponentText("Passive Mobs: " + getPassiveEntityCount(world.loadedEntityList)));
        sender.addChatMessage(new ChatComponentText("Total Living Entities: " + getLivingEntityCount(world.loadedEntityList)));
        sender.addChatMessage(new ChatComponentText("Total Entities: " + world.loadedEntityList.size()));
        sender.addChatMessage(new ChatComponentText("Tile Entities: " + world.loadedTileEntityList.size()));
        sender.addChatMessage(new ChatComponentText("Loaded Chunks: " + this.world.getChunkProvider().getLoadedChunkCount()));
        sender.addChatMessage(new ChatComponentText("TPS: " + timeFormatter.format(worldTickTime) + "ms[" + worldTPS + "]"));
    }

    private int getItemEntityCount(List list) {

        int count = 0;
        for (Entity entity : (ArrayList<Entity>) list) {
            if (entity instanceof EntityItem) {
                count++;
            }
        }
        return count;
    }

    private int getPassiveEntityCount(List list) {

        int count = 0;
        for (Entity entity : (ArrayList<Entity>) list) {
            if (entity instanceof EntityAnimal) {
                count++;
            }
        }
        return count;
    }

    private int getHostileEntityCount(List list) {

        int count = 0;
        for (Entity entity : (ArrayList<Entity>) list) {
            if (entity instanceof EntityMob) {
                count++;
            }
        }
        return count;
    }

    private int getLivingEntityCount(List list) {

        int count = 0;
        for (Entity entity : (ArrayList<Entity>) list) {
            if (entity instanceof EntityLiving) {
                count++;
            }
        }
        return count;
    }

    private String getPlayersForDimension(int dimension) {

        ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().worldServerForDimension(dimension).playerEntities;
        if (players.size() == 0) {
            return "No players in this world";
        } else {
            String playersString = "";
            for (EntityPlayer player : players) {
                playersString = playersString + player.getName() + ", ";
            }
            return playersString.substring(0, playersString.lastIndexOf(playersString) - 2);
        }
    }
}
