package net.quetzi.qutilities.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.quetzi.qutilities.helpers.MovePlayer;

public class CommandFixPlayerPos extends CommandBase {

    List<String> aliases;

    public CommandFixPlayerPos() {

        aliases = new ArrayList<String>();
        aliases.add("qutil playerpos");
    }

    @Override
    public String getCommandName() {

        return "fixplayerpos";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/fixplayerpos";
    }

    @Override
    public List getCommandAliases() {

        aliases.add("qtp");
        aliases.add("tpq");
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] astring) throws NumberInvalidException {

        if (!(astring.length == 0)) {
            if (astring.length == 1) {
                if (MovePlayer.sendToDefaultSpawn(astring[0])) {
                    sender.addChatMessage(new TextComponentString("Moving " + astring[0] + " to their default spawn"));
                } else {
                    sender.addChatMessage((new TextComponentString(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 2) {
                int dim = parseInt(astring[1]);
                if (MovePlayer.sendToDimension(astring[0], dim)) {
                    sender.addChatMessage((new TextComponentString("Moving " + astring[0] + " to dimension " + dim)));
                } else {
                    sender.addChatMessage((new TextComponentString(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 5) {
                int dim = parseInt(astring[1]);
                int x = parseInt(astring[2]);
                int y = parseInt(astring[3]);
                int z = parseInt(astring[4]);
                if (MovePlayer.sendToLocation(astring[0], dim, x, y, z)) {
                    sender.addChatMessage((new TextComponentString("Moving " + astring[0] + " to dimension " + dim + " " + x + ", " + y + ", " +z)));
                } else {
                    sender.addChatMessage((new TextComponentString(astring[0] + " is not online, added to queue")));
                }
            }
        } else {
            getCommandUsage(sender);
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
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }
}
