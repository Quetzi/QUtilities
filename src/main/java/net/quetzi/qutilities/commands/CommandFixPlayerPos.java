package net.quetzi.qutilities.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.quetzi.qutilities.world.MovePlayer;

public class CommandFixPlayerPos implements ICommand {

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
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {
        if (astring[0] != null) {
            MovePlayer.sendToSpawn(astring[0]);
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

    public int getRequiredPermissionLevel() {
        return 3;
    }
}