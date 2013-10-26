package net.quetzi.qutilities.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;

public class DimensionInfoCommand implements ICommand {
	List<String> aliases;
	WorldServer world;
	private static final DecimalFormat timeFormatter = new DecimalFormat(
			"########0.000");

	public DimensionInfoCommand() {
		aliases = new ArrayList<String>();
		aliases.add("qtps");
		aliases.add("tps");
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "diminfo";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/diminfo";
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] args) {
		QUtilities.qLog.info(icommandsender.getCommandSenderName() + " used the command /qtps");
		if (args.length == 0) {
			showTPSSummary(icommandsender);
		} else {
			int dimension;
			try {
				dimension = ((Number) NumberFormat.getInstance().parse(args[0]))
					.intValue();
				} catch (ParseException e1) {
					icommandsender.sendChatToPlayer(new ChatMessageComponent()
						.addText("Invalid dimension ID."));
					return;
				}
			showTPSDetail(icommandsender,dimension);
		}
	}
	
	private void showTPSSummary(ICommandSender sender) {
		int chunksLoaded = 0;
		sender.sendChatToPlayer(ChatMessageComponent.createFromText(UptimeCommand.getUptime()));
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			double worldTickLength = mean(world.getMinecraftServer().worldTickTimes.get(world.provider.dimensionId)) * 1.0E-6D;
			double worldTPS = Math.min(1000.0 / worldTickLength, 20);
			chunksLoaded += world.getChunkProvider().getLoadedChunkCount();
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("[" + world.provider.dimensionId + "]" + world.provider.getDimensionName() + ": " + timeFormatter.format(worldTickLength) + "ms [" + worldTPS + "]"));
		}
		sender.sendChatToPlayer(ChatMessageComponent.createFromText("Total Chunks loaded: " + chunksLoaded));
		sender.sendChatToPlayer(ChatMessageComponent.createFromText("Overall: " + timeFormatter.format(mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D) + "ms [" + Math.min(1000.0 / (mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D),20) + "]"));
	}
	
	private void showTPSDetail(ICommandSender sender, int dimension) {
			MinecraftServer server = MinecraftServer.getServer();
			double worldTickTime = mean(server.worldTickTimes.get(dimension)) * 1.0E-6D;
			double worldTPS = Math.min(1000.0 / worldTickTime, 20);
			this.world = MinecraftServer.getServer().worldServerForDimension(dimension);
			sender.sendChatToPlayer(ChatMessageComponent.createFromText(UptimeCommand.getUptime()));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Information for [" + dimension +"]" + world.provider.getDimensionName()));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Players (" + world.playerEntities.size() + "): " + getPlayersForDimension(dimension)));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Item Entities: " + getItemEntityCount(world.loadedEntityList)));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Hostile Mobs: " + getHostileEntityCount(world.loadedEntityList)));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Passive Mobs: " + getPassiveEntityCount(world.loadedEntityList)));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Total Living Entities: " + getLivingEntityCount(world.loadedEntityList)));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Total Entities: " + world.loadedEntityList.size()));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Tile Entities: " + world.loadedTileEntityList.size()));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("Loaded Chunks: " + this.world.getChunkProvider().getLoadedChunkCount()));
			sender.sendChatToPlayer(ChatMessageComponent.createFromText("TPS: " + timeFormatter.format(worldTickTime) + "ms[" + worldTPS + "]"));
}
	private int getItemEntityCount(List list) {
		int count = 0;
		for (Entity entity : (ArrayList<Entity>) list) {
			if (entity instanceof EntityItem) {
				count++;
			}
		}
		return count;
	}

	private int getPassiveEntityCount(List list) {
		int count = 0;
		for (Entity entity : (ArrayList<Entity>) list) {
			if (entity instanceof EntityAnimal) {
				count++;
			}
		}
		return count;
	}
	
	private int getHostileEntityCount(List list) {
		int count = 0;
		for (Entity entity : (ArrayList<Entity>) list) {
			if (entity instanceof EntityMob) {
				count++;
			}
		}
		return count;
	}
	
	private int getLivingEntityCount(List list) {
		int count = 0;
		for (Entity entity : (ArrayList<Entity>) list) {
			if (entity instanceof EntityLiving) {
				count++;
			}
		}
		return count;
	}
	
	private String getPlayersForDimension(int dimension) {
		ArrayList<EntityPlayer> players = (ArrayList<EntityPlayer>)MinecraftServer.getServer().worldServerForDimension(dimension).playerEntities;
		if (players.size() == 0) {
			return "No players in this world";
		} else {
			String playersString = "";
			Iterator<EntityPlayer> ite = players.iterator();
			while (ite.hasNext()) {
				playersString = playersString + ite.next().username;
				if (ite.hasNext()) {
					playersString = playersString + ",";
				} else {
					playersString = playersString + ".";
				}
			}
			return playersString;
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

	private static long mean(long[] values) {
		long sum = 0l;
		for (long v : values) {
			sum += v;
		}
		return sum / values.length;
	}
}
