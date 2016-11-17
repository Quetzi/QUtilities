package net.quetzi.qutilities;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.quetzi.qutilities.commands.CommandEntity;
import net.quetzi.qutilities.commands.CommandFixPlayerPos;
import net.quetzi.qutilities.commands.CommandPlayerList;
import net.quetzi.qutilities.commands.CommandTPS;
import net.quetzi.qutilities.commands.CommandUUID;
import net.quetzi.qutilities.commands.CommandUptime;
import net.quetzi.qutilities.helpers.References;
import net.quetzi.qutilities.helpers.TeleportQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = References.MODID,
        name = References.NAME,
        version = References.VERSION + "-" + References.BUILD,
        acceptableRemoteVersions = "*",
        acceptedMinecraftVersions = "[1.11,1.12)"
)
public class QUtilities
{
    public static Logger log = LogManager.getLogger("QUtilities");
    public static long          startTime;
    public static boolean       savingEnabled;
    public static int           saveInterval;
    public static String        motd;
    public static boolean       enableMotd;
    public static boolean       enableDeathCounter;
    public static Configuration config;
    public static TeleportQueue queue = new TeleportQueue();

    @EventHandler
    @SideOnly(Side.SERVER)
    public void Init(FMLInitializationEvent event)
    {
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void PreInit(FMLPreInitializationEvent event)
    {
        log = event.getModLog();
        startTime = System.currentTimeMillis();
        // Read configs
        config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();
        String cat = "Settings";
        savingEnabled = config.get(cat, "EnableWorldSaving", false).getBoolean();
        saveInterval = config.get(cat, "SaveInterval", 5, "In minutes").getInt();
        enableMotd = config.get(cat, "EnableMOTD", false).getBoolean();
        enableDeathCounter = config.get(cat, "EnableDeathCounter", false).getBoolean();
        motd = config.getString(cat, "Motd", "Welcome to the Qmunity Subscriber server!", "Set the MOTD when players join the server");

        config.save();
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void PostInit(FMLPostInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new QUtilitesEventHandler());
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void ServerLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandPlayerList());
        event.registerServerCommand(new CommandUptime());
        event.registerServerCommand(new CommandTPS());
        event.registerServerCommand(new CommandFixPlayerPos());
        event.registerServerCommand(new CommandUUID());
        event.registerServerCommand(new CommandEntity());
    }

    @EventHandler
    @SideOnly(Side.SERVER)
    public void ServerStarted(FMLServerStartedEvent event)
    {
        if (enableDeathCounter)
        {
            Scoreboard score = new Scoreboard();
            score.addScoreObjective("deaths", score.getObjective("deathCount").getCriteria());
            score.setObjectiveInDisplaySlot(0, score.getObjective("deathCount"));
        }
    }
}
