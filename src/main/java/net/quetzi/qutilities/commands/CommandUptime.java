package net.quetzi.qutilities.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.QUtilities;

import java.util.ArrayList;
import java.util.List;

public class CommandUptime implements ICommand {

    List<String> aliases;

    public CommandUptime() {

        aliases = new ArrayList<String>();
        aliases.add("qutil uptime");
    }

    public static String getUptime() {

        String uptimeText;
        long uptime = System.currentTimeMillis() - QUtilities.startTime;
        int days = (int) (uptime / (1000 * 60 * 60 * 24)) % 7;
        int hours = (int) (uptime / (1000 * 60 * 60)) % 24;
        int mins = (int) (uptime / (1000 * 60)) % 60;
        int secs = (int) (uptime / 1000) % 60;
        if (days > 0) {
            return String.format("Current uptime: %s days, %sh %sm %ss", days, hours, mins, secs);
        } else if (hours > 0) {
            return String.format("Current uptime: %sh %sm %ss", hours, mins, secs);
        } else {
            return String.format("Current uptime: %sm %ss", mins, secs);
        }
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }

    @Override
    public String getCommandName() {

        return "uptime";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/uptime";
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        icommandsender.addChatMessage(new ChatComponentText(this.getUptime()));
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
