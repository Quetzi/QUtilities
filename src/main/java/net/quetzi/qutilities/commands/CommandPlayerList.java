package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandPlayerList extends CommandBase
{
    private List<String> aliases;

    public CommandPlayerList()
    {
        aliases = new ArrayList<>();
        aliases.add("dimlist");
        aliases.add("qlist");
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "list";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/list";
    }

    @Nonnull
    @Override
    public List<String> getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        String playerList = "";
        for (WorldServer world : server.worlds)
        {
            for (EntityPlayer player : world.playerEntities)
            {
                playerList = playerList + ("[" + player.dimension + "]" + player.getName() + " ");
            }
        }
        if (server.getCurrentPlayerCount() > 0)
        {
            sender.sendMessage(new TextComponentString("Players online: [" + server.getCurrentPlayerCount() + "/" + server.getMaxPlayers() + "]"));
            sender.sendMessage(new TextComponentString(playerList));
        }
        else
        {
            sender.sendMessage(new TextComponentString("No players currently online."));
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

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return false;
    }

}
