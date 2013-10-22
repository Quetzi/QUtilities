package net.quetzi.qutilities.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;

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
		QUtilities.qLog.info("Player " + icommandsender.getCommandSenderName() + " used command: " + astring.toString());
		int dimId;
		if (astring.length > 0) {
			try {
				dimId = ((Number) NumberFormat.getInstance().parse(astring[0]))
						.intValue();
			} catch (ParseException e1) {
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("Invalid dimension ID."));
				return;
			}
			if (MinecraftServer.getServer().worldServerForDimension(dimId) != null) {

				MinecraftServer server = MinecraftServer.getServer();
				worldTickTime = mean(server.worldTickTimes.get(dimId)) * 1.0E-6D;
				worldTPS = Math.min(1000.0 / worldTickTime, 20);
				this.world = MinecraftServer.getServer()
						.worldServerForDimension(dimId);
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText(UptimeCommand.getUptime()));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("Information for dimension: " + dimId));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("World Name: "
								+ world.provider.getDimensionName()));
				icommandsender
						.sendChatToPlayer(ChatMessageComponent
								.createFromText("Players: "
										+ getPlayerString(world.playerEntities)));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("Entities: " + world.loadedEntityList.size()));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("Tile Entities: " + world.loadedTileEntityList.size()));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("Loaded Chunks: "
								+ this.world.getChunkProvider()
										.getLoadedChunkCount()));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("TickTime: "
								+ timeFormatter.format(worldTickTime)));
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("TPS: " + worldTPS));
			}
		} else if (astring.length == 0) {
			for (WorldServer world : MinecraftServer.getServer().worldServers) {
				worldTickTime = mean(world.getMinecraftServer().worldTickTimes
						.get(world.provider.dimensionId)) * 1.0E-6D;
				worldTPS = Math.min(1000.0 / worldTickTime, 20);
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText("[" + world.provider.dimensionId + "]"
								+ world.provider.getDimensionName() + ": "
								+ timeFormatter.format(worldTickTime) + "ms ["
								+ worldTPS + "]: Entities: "
								+ world.loadedEntityList.size()));
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
		}
	}
	private String getPlayerString(List<EntityPlayer> players) {
		String string = "";
		for (EntityPlayer player: players) {
			string = string + player.username + ", ";
		}
		return string;
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
