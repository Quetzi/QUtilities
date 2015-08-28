package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

/**
 * Created by Quetzi on 28/08/15.
 */
public class CommandSpawn extends CommandBase {

    @Override
    public String getCommandName() {

        return "spawn";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/spawn info";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args[0].equalsIgnoreCase("info")) {
            World world = MinecraftServer.getServer().worldServerForDimension(sender.getEntityWorld().provider.dimensionId);
            sender.addChatMessage(new ChatComponentText("Spawn location: " + world.getSpawnPoint().posX + ", " + world.getSpawnPoint().posY + ", " + world.getSpawnPoint().posZ));
        }
    }
}
