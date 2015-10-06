package net.quetzi.qutilities.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Hashtable;

/**
 * Created by Quetzi on 06/10/15.
 */
public class JsonStatWriter {

    public static boolean write() {

        Gson gb = new GsonBuilder().setPrettyPrinting().create();
        Stats params = new Stats();
        try {
            String data = gb.toJson(params);
            File jsonPath = new File(MinecraftServer.getServer().getFolderName(), "serverstats.json");
            if (!jsonPath.exists() || jsonPath.delete()) {
                FileUtils.writeStringToFile(jsonPath, gb.toJson(data));
                return true;
            } else {
                QUtilities.log.warn("Error writing JSON file");
                return false;
            }
        } catch (Exception e) {
            QUtilities.log.warn("Error writing JSON file");
            QUtilities.log.warn(e.getStackTrace());
        }
        return false;
    }
}

class Stats {

    long                      timestamp   = System.currentTimeMillis();
    String                    name        = MinecraftServer.getServer().getServerHostname();
    String                    motd        = MinecraftServer.getServer().getMotd();
    boolean                   isPvp       = MinecraftServer.getServer().isPVPEnabled();
    String                    gamemode    = MinecraftServer.getServer().getGameType().getName();
    String                    uptime      = SystemInfo.getUptime();
    int                       playerCount = MinecraftServer.getServer().getCurrentPlayerCount();
    int                       maxPlayers  = MinecraftServer.getServer().getMaxPlayers();
    Hashtable<String, Double> tps         = new Hashtable<String, Double>();

    public Stats() {

        for (WorldServer server : MinecraftServer.getServer().worldServers) {
            tps.put(server.getWorldInfo().getWorldName(), SystemInfo.getWorldTickTime(server));
        }
    }
}