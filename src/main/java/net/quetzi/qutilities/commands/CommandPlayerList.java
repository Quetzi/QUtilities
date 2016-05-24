package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        String playerList = "";
        for (WorldServer world : server.worldServers) {
            for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
                playerList = playerList + ("[" + player.dimension + "]" + player.getName() + " ");
            }
        }
        if (server.getCurrentPlayerCount() > 0) {
            sender.addChatMessage(new TextComponentString("Players online: [" + server.getCurrentPlayerCount() + "/" + server.getMaxPlayers() + "]"));
            sender.addChatMessage(new TextComponentString(playerList));
        } else {
            sender.addChatMessage(new TextComponentString("No players currently online."));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

        return true;
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] astring, BlockPos pos) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i) {

        return false;
    }

}
