package net.quetzi.qutilities.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.server.command.ForgeCommand;

public class DimensionInfoCommand implements ICommand {
	List<String> aliases;
	WorldServer world;
	double worldTickTime;
	double worldTPS;
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
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		int dimId;
		try {
			dimId = ((Number) NumberFormat.getInstance().parse(astring[0]))
					.intValue();
		} catch (ParseException e1) {
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Invalid dimension ID."));
			return;
		}

		if (astring.length == 0) {
			for (WorldServer world : MinecraftServer.getServer().worldServers) {
				worldTickTime = mean(world.getMinecraftServer().worldTickTimes
						.get(world.provider.dimensionId)) * 1.0E-6D;
				worldTPS = Math.min(1000.0 / worldTickTime, 20);
				icommandsender.sendChatToPlayer(new ChatMessageComponent()
						.addText("["
								+ world.provider.dimensionId
								+ "]"
								+ world.provider.getDimensionName()
								+ ": "
								+ timeFormatter.format(worldTickTime)
								+ "ms ["
								+ worldTPS
								+ "]"
								+ ": Entities: "
								+ world.loadedEntityList.size()
								+ ": Chunks loaded: "
								+ world.getChunkProvider()
										.getLoadedChunkCount()
										));
			}
			icommandsender
					.sendChatToPlayer(new ChatMessageComponent().addText("Overall: "
							+ timeFormatter.format(mean(MinecraftServer
									.getServer().tickTimeArray) * 1.0E-6D)
							+ "ms ["
							+ Math.min(
									1000.0 / (mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D),
									20) + "]"));
			return;
		} else if (MinecraftServer.getServer().worldServerForDimension(dimId) != null) {

			MinecraftServer server = MinecraftServer.getServer();
			worldTickTime = mean(server.worldTickTimes.get(dimId)) * 1.0E-6D;
			worldTPS = Math.min(1000.0 / worldTickTime, 20);
			this.world = MinecraftServer.getServer().worldServers[dimId];

			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Information for dimension: " + dimId));
			icommandsender
					.sendChatToPlayer(new ChatMessageComponent()
							.addText("World Name: "
									+ world.provider.getDimensionName()));
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Players: " + world.playerEntities.toString()));
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Entities: " + world.loadedEntityList.size()));
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Loaded Chunks: "
							+ this.world.getChunkProvider()
									.getLoadedChunkCount()));
			icommandsender
					.sendChatToPlayer(new ChatMessageComponent()
							.addText("TickTime: "
									+ timeFormatter.format(worldTickTime)));
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("TPS: " + worldTPS));
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
