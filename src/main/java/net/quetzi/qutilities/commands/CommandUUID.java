package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {

        if (!(args.length == 0)) {
            String UUID = server.getPlayerProfileCache().getGameProfileForUsername(args[0].toLowerCase()).getId().toString();
            sender.addChatMessage(new TextComponentString("UUID for " + args[0] + ": " + UUID));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }
}