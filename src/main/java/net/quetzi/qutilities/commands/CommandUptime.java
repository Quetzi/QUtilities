package net.quetzi.qutilities.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.SystemInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandUptime implements ICommand {

    List<String> aliases;

    public CommandUptime() {

        aliases = new ArrayList<String>();
        aliases.add("qutil uptime");
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

        icommandsender.addChatMessage(new ChatComponentText(SystemInfo.getUptime()));
        icommandsender.addChatMessage(new ChatComponentText(SystemInfo.getAllocatedMem() + "/" + SystemInfo.getMaxMem() + "[" + SystemInfo.getPercentMemUse() + "%]"));

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
