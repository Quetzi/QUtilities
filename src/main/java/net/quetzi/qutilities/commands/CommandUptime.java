package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.quetzi.qutilities.helpers.SystemInfo;

import java.util.ArrayList;
import java.util.List;

public class CommandUptime extends CommandBase
{
    List<String> aliases;

    public CommandUptime()
    {
        aliases = new ArrayList<String>();
        aliases.add("qutil uptime");
    }

    @Override
    public String getCommandName()
    {
        return "uptime";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/uptime";
    }

    @Override
    public List getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender icommandsender, String[] astring)
    {
        icommandsender.addChatMessage(new TextComponentString(SystemInfo.getUptime()));
        icommandsender.addChatMessage(new TextComponentString("Memory usage: " + SystemInfo.getAllocatedMem() + "/" + SystemInfo.getMaxMem() + "[" + SystemInfo.getPercentMemUse() + "%]"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }
}
