package net.quetzi.qutilities.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.MovePlayer;

public class CommandFixPlayerPos implements ICommand {

    List<String> aliases;

    public CommandFixPlayerPos() {

        aliases = new ArrayList<String>();
        aliases.add("qutil playerpos");
    }

    @Override
    public int compareTo(Object o) {

        return 0;
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

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (!(astring.length == 0)) {
            if (astring.length == 1) {
                MovePlayer.sendToDefaultSpawn(astring[0]);
                icommandsender.addChatMessage(new ChatComponentText("Moving " + astring[0] + " to their default spawn"));
            }
            if (astring.length == 2) {
                int dim = Integer.getInteger(astring[1]);
                MovePlayer.sendToDimension(astring[0], dim);
            }
            if (astring.length == 5) {
                int dim = Integer.getInteger(astring[1]);
                int x = Integer.getInteger(astring[2]);
                int y = Integer.getInteger(astring[3]);
                int z = Integer.getInteger(astring[4]);
                MovePlayer.sendToLocation(astring[0], dim, x, y, z);
            }
        } else {
            getCommandUsage(icommandsender);
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

    public int getRequiredPermissionLevel() {

        return 3;
    }
}
