package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by Quetzi on 12/02/15.
 */
public class CommandUUID extends CommandBase {

    @Override
    public String getCommandName() {

        return "getuuid";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return "/getuuid <player>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (!(args.length == 0)) {
            String UUID = MinecraftServer.getServer().func_152358_ax().func_152655_a(args[1].toLowerCase()).getId().toString();
            sender.addChatMessage(new ChatComponentText("UUID for " + args[0] + ": " + UUID));
        }
    }
}
