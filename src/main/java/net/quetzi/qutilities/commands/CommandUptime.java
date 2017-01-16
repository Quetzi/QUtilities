package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.quetzi.qutilities.helpers.SystemInfo;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandUptime extends CommandBase
{
    private List<String> aliases;

    public CommandUptime()
    {
        aliases = new ArrayList<>();
        aliases.add("qutil uptime");
    }

    @Nonnull
    @Override
    public String getCommandName()
    {
        return "uptime";
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender sender)
    {
        return "/uptime";
    }

    @Nonnull
    @Override
    public List<String> getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        sender.addChatMessage(new TextComponentString(SystemInfo.getUptime()));
        sender.addChatMessage(new TextComponentString("Memory usage: " + SystemInfo.getAllocatedMem() + "/" + SystemInfo.getMaxMem() + "[" + SystemInfo.getPercentMemUse() + "%]"));
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return new ArrayList<>();
    }
}
