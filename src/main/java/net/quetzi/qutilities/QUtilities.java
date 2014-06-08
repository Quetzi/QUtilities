package net.quetzi.qutilities;

import net.minecraftforge.common.config.Configuration;
import net.quetzi.qutilities.commands.CommandFixPlayerPos;
import net.quetzi.qutilities.commands.CommandPlayerList;
import net.quetzi.qutilities.commands.CommandTPS;
import net.quetzi.qutilities.commands.CommandUptime;
import net.quetzi.qutilities.references.References;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION + "-"
        + References.BUILD, acceptableRemoteVersions = "*")
public class QUtilities {
    public static Logger qLog = LogManager.getLogger("QUtilities");
    public static long startTime;
    public static boolean savingEnabled;
    public static int saveInterval;

    @EventHandler
    @SideOnly(Side.SERVER)
    public void Init(FMLInitializationEvent event) {
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void PreInit(FMLPreInitializationEvent event) {
        startTime = System.currentTimeMillis();
        // Read configs
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        savingEnabled = config.get("Settings", "EnableWorldSaving", true).getBoolean(true);
        saveInterval = config.get("Settings", "SaveInterval", 5, "In Minutes").getInt();
        config.save();
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void PostInit(FMLPostInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new QUtilitesEventHandler());
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPlayerList());
        event.registerServerCommand(new CommandUptime());
        event.registerServerCommand(new CommandTPS());
        event.registerServerCommand(new CommandFixPlayerPos());
    }
}