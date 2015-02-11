package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.MovePlayer;
import net.quetzi.qutilities.helpers.TeleportQueue;

import java.util.ArrayList;
import java.util.List;

public class CommandFixPlayerPos extends CommandBase {

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

        aliases.add("qtp");
        aliases.add("tpq");
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (!(astring.length == 0)) {
            if (astring.length == 1) {
                if (astring[0].equalsIgnoreCase("showqueue")) {
                    if (TeleportQueue.getQueue().size() > 0) {
                        for (String line : TeleportQueue.getQueue()) {
                            icommandsender.addChatMessage(new ChatComponentText(line));
                        }
                    }
                    return;
                }
                else if (MovePlayer.sendToDefaultSpawn(astring[0])) {
                    icommandsender.addChatMessage(new ChatComponentText("Moving " + astring[0] + " to their default spawn"));
                } else {
                    icommandsender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 2) {
                int dim = parseInt(icommandsender, astring[1]);
                if (MovePlayer.sendToDimension(astring[0], dim)) {
                    icommandsender.addChatMessage((new ChatComponentText("Moving " + astring[0] + " to dimension " + dim)));
                } else {
                    icommandsender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 5) {
                int dim = parseInt(icommandsender, astring[1]);
                int x = parseInt(icommandsender, astring[2]);
                int y = parseInt(icommandsender, astring[3]);
                int z = parseInt(icommandsender, astring[4]);
                if (MovePlayer.sendToLocation(astring[0], dim, x, y, z)) {
                    icommandsender.addChatMessage((new ChatComponentText("Moving " + astring[0] + " to dimension " + dim + " " + x + ", " + y + ", " +z)));
                } else {
                    icommandsender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
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

    @Override
    public int getRequiredPermissionLevel() {

        return 3;
    }
}
