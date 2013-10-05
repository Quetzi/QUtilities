package net.quetzi.qutilities.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldServer;

public class QUtilitiesCommand implements ICommand {
	private List<String> aliases;

	public QUtilitiesCommand() {
		aliases = new ArrayList<String>();
		aliases.add("qutilities");
		aliases.add("qutils");
		aliases.add("q");
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "qutils";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/qutils <args>";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] args) {
		if (args.length == 0) {

		} else if ("removedrops".equals(args[0])) {
			icommandsender.sendChatToPlayer(new ChatMessageComponent().addText("Marked " + removeDrops() + " entities for deletion."));
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	private int removeDrops() {
		int itemsRemoved = 0;
		MinecraftServer server = MinecraftServer.getServer();
		for (WorldServer world : server.worldServers) {
			for (Entity entity : (ArrayList<Entity>) world
					.getLoadedEntityList()) {
				if (entity.onGround && (entity.getClass() == EntityItem.class)) {
					world.removeEntity(entity);
					itemsRemoved++;
				}
			}
		}
		return itemsRemoved;
	}
}
