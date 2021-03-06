package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.quetzi.qutilities.helpers.MovePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandFixPlayerPos extends CommandBase
{
    private List<String> aliases;

    public CommandFixPlayerPos()
    {
        aliases = new ArrayList<>();
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "fixplayerpos";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/fixplayerpos";
    }

    @Nonnull
    @Override
    public List<String> getAliases()
    {
        aliases.add("qtp");
        aliases.add("tpq");
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws NumberInvalidException
    {
        if (!(args.length == 0))
        {
            if (args.length == 1)
            {
                if (MovePlayer.sendToDefaultSpawn(args[0]))
                {
                    sender.sendMessage(new TextComponentString("Moving " + args[0] + " to their default spawn"));
                }
                else
                {
                    sender.sendMessage((new TextComponentString(args[0] + " is not online, added to queue")));
                }
            }
            if (args.length == 2)
            {
                int dim = parseInt(args[1]);
                if (MovePlayer.sendToDimension(args[0], dim))
                {
                    sender.sendMessage((new TextComponentString("Moving " + args[0] + " to dimension " + dim)));
                }
                else
                {
                    sender.sendMessage((new TextComponentString(args[0] + " is not online, added to queue")));
                }
            }
            if (args.length == 5)
            {
                int dim = parseInt(args[1]);
                int x   = parseInt(args[2]);
                int y   = parseInt(args[3]);
                int z   = parseInt(args[4]);
                if (MovePlayer.sendToLocation(args[0], dim, x, y, z))
                {
                    sender.sendMessage((new TextComponentString("Moving " + args[0] + " to dimension " + dim + " " + x + ", " + y + ", " + z)));
                }
                else
                {
                    sender.sendMessage((new TextComponentString(args[0] + " is not online, added to queue")));
                }
            }
        }
        else
        {
            getUsage(sender);
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return new ArrayList<>();
    }
}