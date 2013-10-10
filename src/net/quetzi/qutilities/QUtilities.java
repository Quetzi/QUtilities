package net.quetzi.qutilities;

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.quetzi.qutilities.commands.DimensionInfoCommand;
import net.quetzi.qutilities.commands.PlayerListingCommand;
import net.quetzi.qutilities.commands.UptimeCommand;
import net.quetzi.qutilities.references.References;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class QUtilities {
	public static Logger qLog = Logger.getLogger("QUtilities");
	public static long startTime;
	
	@EventHandler
	@SideOnly(Side.SERVER)
	public void Init(FMLInitializationEvent event) {
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PreInit(FMLPreInitializationEvent event) {
		startTime = System.currentTimeMillis();
		qLog.setParent(FMLLog.getLogger());
		qLog.info("Loading configuration");
		// Read configs
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		config.save();
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PostInit(FMLPostInitializationEvent event) {
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new PlayerListingCommand());
		event.registerServerCommand(new UptimeCommand());
		event.registerServerCommand(new DimensionInfoCommand());
	}
}