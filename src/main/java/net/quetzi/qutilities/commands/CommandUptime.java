package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.SystemInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandUptime extends CommandBase {

    List<String> aliases;

    public CommandUptime() {

        aliases = new ArrayList<String>();
        aliases.add("qutil uptime");
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
        icommandsender.addChatMessage(new ChatComponentText("Memory usage: " + SystemInfo.getAllocatedMem() + "/" + SystemInfo.getMaxMem() + "[" + SystemInfo.getPercentMemUse() + "%]"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring, BlockPos pos) {

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
