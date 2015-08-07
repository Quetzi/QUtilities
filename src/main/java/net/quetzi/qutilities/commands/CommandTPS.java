package net.quetzi.qutilities.commands;

import cpw.mods.fml.common.Optional;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.helpers.References;
import net.quetzi.qutilities.helpers.SystemInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Optional.Interface(iface = "irclib.IRCCommand", modid = References.FORGEIRC)
public class CommandTPS extends CommandBase {

    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");
    List<String> aliases    = new ArrayList<String>();
    List<String> textOutput = new ArrayList<String>();

    public CommandTPS() {

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
    public String getCommandName() {

        return "diminfo";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/diminfo";
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void processCommand(ICommandSender icommandsender, String[] args) {

        if (args.length == 0) {
            sendOutput(icommandsender, getTPSSummary());
        } else if (args[0].equalsIgnoreCase("entities")) {
            HashMap<String, Integer> entities = getEntityTypeCount();
            Iterator iterator = entities.entrySet().iterator();
            while (iterator.hasNext()) {
                icommandsender.addChatMessage(new ChatComponentText(((Map.Entry<String, Integer>) iterator.next()).getKey() + ": " + ((Map.Entry<String, Integer>) iterator.next()).getValue()));
            }
            return;
        } else {
            int dimension;
            try {
                dimension = NumberFormat.getInstance().parse(args[0]).intValue();
            } catch (ParseException e1) {
                icommandsender.addChatMessage(new ChatComponentText("Invalid dimension ID."));
                return;
            }
            sendOutput(icommandsender, getTPSDetail(dimension));
        }
    }

    private void sendOutput(ICommandSender sender, List<String> text) {

        for (String line : text) {
            sender.addChatMessage(new ChatComponentText(line));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 0;
    }

    private List<String> getTPSSummary() {

        textOutput = new ArrayList<String>();
        int chunksLoaded = 0;
        textOutput.add(SystemInfo.getUptime());
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            chunksLoaded += world.getChunkProvider().getLoadedChunkCount();
            textOutput.add("[" + world.provider.dimensionId + "]" + world.provider.getDimensionName() + ": " + timeFormatter.format(SystemInfo.getWorldTickTime(world)) + "ms [" + timeFormatter.format(SystemInfo.getDimensionTPS(world))
                           + "]");
        }
        textOutput.add("Total Chunks loaded: " + chunksLoaded);
        textOutput.add("Overall: " + timeFormatter.format(mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D) + "ms ["
                       + Math.min(1000.0 / (mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D), 20) + "]");
        return textOutput;
    }

    @SuppressWarnings("unchecked")
    private List<String> getTPSDetail(int dimension) {

        textOutput = new ArrayList<String>();
        WorldServer world = MinecraftServer.getServer().worldServerForDimension(dimension);
        textOutput.add(SystemInfo.getUptime());
        textOutput.add("Information for [" + dimension + "]" + world.provider.getDimensionName());
        textOutput.add("Players (" + world.playerEntities.size() + "): " + getPlayersForDimension(dimension));
        textOutput.add("Item Entities: " + getItemEntityCount((ArrayList<Entity>) world.loadedEntityList));
        textOutput.add("Hostile Mobs: " + getHostileEntityCount((ArrayList<Entity>) world.loadedEntityList));
        textOutput.add("Passive Mobs: " + getPassiveEntityCount((ArrayList<Entity>) world.loadedEntityList));
        textOutput.add("Total Living Entities: " + getLivingEntityCount((ArrayList<Entity>) world.loadedEntityList));
        textOutput.add("Total Entities: " + world.loadedEntityList.size());
        textOutput.add("Tile Entities: " + world.loadedTileEntityList.size());
        textOutput.add("Loaded Chunks: " + world.getChunkProvider().getLoadedChunkCount());
        textOutput.add("TPS: " + timeFormatter.format(SystemInfo.getWorldTickTime(world)) + "ms[" + SystemInfo.getDimensionTPS(world) + "]");
        return textOutput;
    }

    private int getItemEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityItem) {
                count++;
            }
        }
        return count;
    }

    private int getPassiveEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityAnimal) {
                count++;
            }
        }
        return count;
    }

    private int getHostileEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityMob) {
                count++;
            }
        }
        return count;
    }

    private int getLivingEntityCount(ArrayList<Entity> list) {

        int count = 0;
        for (Entity entity : list) {
            if (entity instanceof EntityLiving) {
                count++;
            }
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    private String getPlayersForDimension(int dimension) {

        ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>) MinecraftServer.getServer().worldServerForDimension(dimension).playerEntities;
        if (players.size() == 0) {
            return "No players in this world";
        } else {
            String playersString = "";
            Iterator<EntityPlayer> ite = players.iterator();
            while (ite.hasNext()) {
                playersString = playersString + ite.next().getCommandSenderName();
                if (ite.hasNext()) {
                    playersString = playersString + ",";
                } else {
                    playersString = playersString + ".";
                }
            }
            return playersString;
        }
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Integer> getEntityTypeCount() {

        HashMap<String, Integer> entityList = new HashMap<String, Integer>();
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            for (Entity entity : (ArrayList<Entity>) world.loadedEntityList) {
                if (entityList.get(entity.getClass().getName()) == null) {
                    entityList.put(entity.getClass().getName(), 1);
                } else {
                    entityList.put(entity.getClass().getName(), entityList.get(entity.getClass().getName()) + 1);
                }
            }
        }
        return entityList;
    }
}
