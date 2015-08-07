package net.quetzi.qutilities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.config.Configuration;
import net.quetzi.qutilities.commands.*;
import net.quetzi.qutilities.helpers.References;
import net.quetzi.qutilities.helpers.TeleportQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION + "-" + References.BUILD, acceptableRemoteVersions = "*")
public class QUtilities {

    public static Logger log = LogManager.getLogger("QUtilities");
    public static long          startTime;
    public static boolean       savingEnabled;
    public static int           saveInterval;
    public static String        motd;
    public static boolean       enableMotd;
    public static Configuration config;
    public static TeleportQueue queue = new TeleportQueue();

    @EventHandler
    @SideOnly(Side.SERVER)
    public void Init(FMLInitializationEvent event) {

    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void PreInit(FMLPreInitializationEvent event) {

        log = event.getModLog();
        startTime = System.currentTimeMillis();
        // Read configs
        config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        savingEnabled = config.get("Settings", "EnableWorldSaving", false).getBoolean(false);
        saveInterval = config.get("Settings", "SaveInterval", 5, "In minutes").getInt();
        enableMotd = config.get("Settings", "EnableMOTD", false).getBoolean(false);
        motd = config.getString("Settings", "Motd", "Welcome to the Qmunity Subscriber server!", "Set the MOTD when players join the server");
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
        event.registerServerCommand(new CommandUUID());
//        event.registerServerCommand(new CommandListEntities());

//        if (Loader.isModLoaded(References.FORGEIRC)) {
//            IRCLib ircBot = new IRCLib();
//            ircBot.registerCommand("!qtps", new CommandTPS());
//            ircBot.registerCommand("!uptime", new CommandUptime());
//        }
    }
}
