package net.quetzi.qutilities.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quetzi on 06/10/15.
 */
public class JsonStatWriter {

    public static boolean write() {

        Gson gb = new GsonBuilder().create();
        Stats params = new Stats();
        try {
            String data = gb.toJson(params);
            File jsonPath = new File(MinecraftServer.getServer().getFolderName(), ".." + File.separator + "serverstats.json");
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

    long                           timestamp        = System.currentTimeMillis();
    String                         name             = MinecraftServer.getServer().getServerHostname();
    String                         motd             = MinecraftServer.getServer().getMotd();
    boolean                        isPvp            = MinecraftServer.getServer().isPVPEnabled();
    String                         gamemode         = MinecraftServer.getServer().getGameType().getName();
    String                         uptime           = SystemInfo.getUptime();
    int                            playerCount      = MinecraftServer.getServer().getCurrentPlayerCount();
    int                            maxPlayers       = MinecraftServer.getServer().getMaxPlayers();
    String                         minecraftVersion = MinecraftServer.getServer().getMinecraftVersion();
    ArrayList<Map<String, String>> dimensions       = new ArrayList<Map<String, String>>();


    public Stats() {

        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        Map<String, String> dim = new HashMap<String, String>();

        for (WorldServer server : MinecraftServer.getServer().worldServers) {
            dim.put("name", server.provider.getDimensionName());
            dim.put("tps", String.valueOf(SystemInfo.getDimensionTPS(server)) + "ms");
            dim.put("daytime", String.valueOf(server.isDaytime()));
            dim.put("raining", String.valueOf(server.getWorldInfo().isRaining()));
            dim.put("thunder", String.valueOf(server.getWorldInfo().isThundering()));
            dim.put("worldtime", String.valueOf(server.getWorldInfo().getWorldTime()));
            dimensions.add(dim);
        }
    }
}