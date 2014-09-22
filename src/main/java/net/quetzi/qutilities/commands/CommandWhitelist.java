/*
 * @author Quetzi
 */

package net.quetzi.qutilities.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.QUtilities;
import net.quetzi.qutilities.world.Whitelist;

import java.util.ArrayList;
import java.util.List;

public class CommandWhitelist implements ICommand {

    private List<String> aliases;

    public CommandWhitelist() {

        aliases = new ArrayList<String>();
    }

    @Override
    public int compareTo(Object arg0) {

        return 0;
    }

    @Override
    public String getCommandName() {

        return "qw";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {

        return "Syntax: /qw reload, /qw enable, /qw disable, /qw export, /qw list";
    }

    @Override
    public List getCommandAliases() {

        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                new Thread(new Whitelist()).start();
            } else if (args[0].equalsIgnoreCase("enable")) {
                QUtilities.whitelistEnabled = true;
                QUtilities.config.get("Settings", "WhitelistEnabled", false).set(true);
                QUtilities.config.save();
                commandSender.addChatMessage(new ChatComponentText("Remote whitelist enabled."));
            } else if (args[0].equalsIgnoreCase("disable")) {
                QUtilities.whitelistEnabled = false;
                QUtilities.config.get("Settings", "WhitelistEnabled", false).set(false);
                QUtilities.config.save();
                commandSender.addChatMessage(new ChatComponentText("Remote whitelist disabled."));
            } else if (args[0].equalsIgnoreCase("export")) {
                Whitelist.writeWhitelist();
                commandSender.addChatMessage(new ChatComponentText("Remote whitelist written to whitelist-export.txt."));
            } else if (args[0].equalsIgnoreCase("list")) {
                String list = "Users: ";
                for (String user : QUtilities.whitelist)
                    list = list + user + ", ";
                commandSender.addChatMessage(new ChatComponentText(list));
            }
        } else commandSender.addChatMessage(new ChatComponentText("Syntax: /qw reload, /qw enable, /qw disable, /qw export, /qw list"));

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender var1, String[] var2) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {

        return false;
    }

    public int getRequiredPermissionLevel() {

        return 3;
    }
}
