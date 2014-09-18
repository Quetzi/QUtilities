package net.quetzi.qutilities;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.config.Configuration;
import net.quetzi.qutilities.commands.CommandFixPlayerPos;
import net.quetzi.qutilities.commands.CommandPlayerList;
import net.quetzi.qutilities.commands.CommandTPS;
import net.quetzi.qutilities.commands.CommandUptime;
import net.quetzi.qutilities.commands.CommandWhitelist;
import net.quetzi.qutilities.references.References;
import net.quetzi.qutilities.world.Whitelist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION + "-" + References.BUILD, acceptableRemoteVersions = "*")
public class QUtilities {

    public static Logger        log              = LogManager.getLogger("QUtilities");
    public static long          startTime;
    public static boolean       savingEnabled;
    public static int           saveInterval;
    public static String        uniqueID;
    public static boolean       whitelistEnabled = false;
    public static Set<String>   whitelist        = new HashSet();
    public static int           checkInterval;
    public static boolean       secondaryWhitelistEnabled = false;
    public static String        secondaryWhitelistLocation;
    public static String        kickMessage;
    public static Configuration config;

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
        savingEnabled = config.get("Settings", "EnableWorldSaving", true).getBoolean(true);
        saveInterval = config.get("Settings", "SaveInterval", 5, "In minutes").getInt();
        whitelistEnabled = config.get("Settings", "WhitelistEnabled", false).getBoolean(false);
        secondaryWhitelistEnabled = config.get("Settings", "SecondaryWhitelistEnabled", false).getBoolean(false);
        secondaryWhitelistLocation = config.get("Settings", "SecondaryWhitelistLocation", "http://your.url.here/whitelist.txt").getString();
        uniqueID = config.get("Settings", "UniqueID", "CHANGEME").getString();
        checkInterval = config.get("Settings", "CheckInterval", 10, "In minutes").getInt();
        kickMessage = config.get("Settings", "KickMessage", "You are not a current Twitch Subscriber or Patron, if this is wrong wait a few minutes").getString();
        config.save();

        if ((this.uniqueID == null) || (this.uniqueID.equalsIgnoreCase("CHANGEME"))) {
            log.info("Please set your unique ID in qutilities.cfg and restart your server.");
        } else {
            if (Whitelist.updateWhitelist()) {
                this.whitelistEnabled = config.get("Settings", "Enabled", false).getBoolean(false);
            }
        }
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
        event.registerServerCommand(new CommandWhitelist());
    }
}
