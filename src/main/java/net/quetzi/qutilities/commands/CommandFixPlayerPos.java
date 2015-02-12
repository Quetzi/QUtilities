package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.QUtilities;
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
    public void processCommand(ICommandSender sender, String[] astring) {

        if (!(astring.length == 0)) {
            if (astring.length == 1) {
                if (astring[0].equalsIgnoreCase("showqueue")) {
                    if (QUtilities.queue.getQueue().size() > 0) {
                        for (String line : QUtilities.queue.getQueue()) {
                            sender.addChatMessage(new ChatComponentText(line));
                        }
                    } else {
                        sender.addChatMessage(new ChatComponentText("No players in the teleport queue"));
                    }
                    return;
                }
                else if (astring[0].equalsIgnoreCase("clearqueue")) {
                    if (QUtilities.queue.getQueue().size() > 0) {
                        QUtilities.queue = new TeleportQueue();
                        sender.addChatMessage(new ChatComponentText("Queue has been cleared"));
                    }
                    return;
                }
                else if (MovePlayer.sendToDefaultSpawn(astring[0])) {
                    sender.addChatMessage(new ChatComponentText("Moving " + astring[0] + " to their default spawn"));
                } else {
                    sender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 2) {
                int dim = parseInt(sender, astring[1]);
                if (MovePlayer.sendToDimension(astring[0], dim)) {
                    sender.addChatMessage((new ChatComponentText("Moving " + astring[0] + " to dimension " + dim)));
                } else {
                    sender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
            }
            if (astring.length == 5) {
                int dim = parseInt(sender, astring[1]);
                int x = parseInt(sender, astring[2]);
                int y = parseInt(sender, astring[3]);
                int z = parseInt(sender, astring[4]);
                if (MovePlayer.sendToLocation(astring[0], dim, x, y, z)) {
                    sender.addChatMessage((new ChatComponentText("Moving " + astring[0] + " to dimension " + dim + " " + x + ", " + y + ", " + z)));
                } else {
                    sender.addChatMessage((new ChatComponentText(astring[0] + " is not online, added to queue")));
                }
            }
        } else {
            getCommandUsage(sender);
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
