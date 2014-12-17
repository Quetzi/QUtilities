package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.ChunkTools;

/**
 * Created by Quetzi on 17/12/14.
 */
public class CommandPreGen extends CommandBase {

    @Override
    public String getCommandName() {

        return "pregen";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/pregen square <dim> <x> <z> <radius> or /pregen rect <dim> <startX> <startZ> <endX> <endZ>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {

        if (args[0].equalsIgnoreCase("square")) {
            if (args.length != 5) {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
            ChunkTools.queueSquare(MinecraftServer.getServer().worldServerForDimension(parseInt(sender, args[1])).provider, parseInt(sender, args[2]), parseInt(sender, args[3]), parseInt(sender, args[4]));
            sender.addChatMessage(new ChatComponentText("Queued " + ChunkTools.getQueueSize() + " chunks for generation. Use /regen start to begin procesing."));
        }
        if (args[0].equalsIgnoreCase("rect")) {
            if (args.length != 6) {
                sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
            }
            ChunkTools.queueRect(MinecraftServer.getServer().worldServerForDimension(parseInt(sender, args[1])).provider, parseInt(sender, args[2]), parseInt(sender, args[3]), parseInt(sender, args[4]), parseInt(sender, args[5]));
            sender.addChatMessage(new ChatComponentText("Queued " + ChunkTools.getQueueSize() + " chunks for generation. Use /regen start to begin procesing."));
        }
        if (args[0].equalsIgnoreCase("start")) {
            ChunkTools.processQueue = true;
            sender.addChatMessage(new ChatComponentText("Started processing  " + ChunkTools.getQueueSize() + " chunks. Use /regen stop to halt procesing."));
        }
        if (args[0].equalsIgnoreCase("stop")) {
            ChunkTools.processQueue = false;
            sender.addChatMessage(new ChatComponentText("Stopped processing.  " + ChunkTools.getQueueSize() + " chunks remaining. Use /regen start to restart."));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }
}