package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by Quetzi on 27/02/15.
 */
public class CommandWhois extends CommandBase {

    @Override
    public String getCommandName() {

        return "whois";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "Usage: /whois <player>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        for (EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            if (args[0].equalsIgnoreCase(player.getGameProfile().getName())) {
                sender.addChatMessage(new ChatComponentText("Player: " + player.getGameProfile().getName()));
                sender.addChatMessage(new ChatComponentText("Display Name: " + player.getDisplayName()));
                sender.addChatMessage(new ChatComponentText("Dimension: " + player.getEntityWorld().provider.dimensionId));
                sender.addChatMessage(new ChatComponentText("Location: " + player.serverPosX + ", " + player.serverPosY + ", " + player.serverPosZ));
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 3;
    }
}
