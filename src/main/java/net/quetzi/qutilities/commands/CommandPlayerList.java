package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class CommandPlayerList extends CommandBase {

    private List<String> aliases;

    public CommandPlayerList() {

        aliases = new ArrayList<String>();
        aliases.add("dimlist");
        aliases.add("qlist");
    }

    @Override
    public String getCommandName() {

        return "list";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/list";
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        String playerList = "";
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
                playerList = playerList + ("[" + player.dimension + "]" + player.getName() + " ");
            }
        }
        if (MinecraftServer.getServer().getCurrentPlayerCount() > 0) {
            icommandsender.addChatMessage(new ChatComponentText("Players online: [" + MinecraftServer.getServer().getCurrentPlayerCount() + "/" + MinecraftServer.getServer().getMaxPlayers() + "]"));
            icommandsender.addChatMessage(new ChatComponentText(playerList));
        } else {
            icommandsender.addChatMessage(new ChatComponentText("No players currently online."));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

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

}
