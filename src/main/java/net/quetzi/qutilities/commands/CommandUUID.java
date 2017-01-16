package net.quetzi.qutilities.commands;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

/**
 * Created by Quetzi on 12/02/15.
 */
public class CommandUUID extends CommandBase
{
    @Nonnull
    @Override
    public String getName()
    {
        return "getuuid";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/getuuid <player>";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        if (!(args.length == 0))
        {
            GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(args[0]);
            String UUID = profile != null ? profile.getId().toString() : "No UUID found";
            sender.sendMessage(new TextComponentString("UUID for " + args[0] + ": " + UUID));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }
}