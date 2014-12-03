package net.quetzi.qutilities.helpers;

/**
 * Created by Quetzi on 01/12/14.
 */
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.quetzi.qutilities.QUtilities;

import java.text.NumberFormat;

public class SystemInfo {

    private static Runtime runtime = Runtime.getRuntime();
    private static NumberFormat format = NumberFormat.getInstance();

    public static String getOSname() {
        return System.getProperty("os.name");
    }

    public static String getOSversion() {
        return System.getProperty("os.version");
    }

    public static String getOsArch() {
        return System.getProperty("os.arch");
    }

    public static String getTotalMem() {
        return format.format(runtime.totalMemory());
    }

    public static String getUsedMem() {
        return format.format(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public static String getFreeMem() {
        return format.format(runtime.freeMemory() / 1024);
    }

    public static String getAllocatedMem() {
        return format.format(runtime.totalMemory() / 1024);
    }

    public static String getMaxMem() {
        return format.format(runtime.maxMemory() / 1024);
    }

    public static String getPercentMemUse() {
        return format.format(runtime.totalMemory() / runtime.maxMemory() * 1000);
    }

    public static String getUptime() {

        long uptime = System.currentTimeMillis() - QUtilities.startTime;
        int days = (int) (uptime / (1000 * 60 * 60 * 24)) % 7;
        int hours = (int) (uptime / (1000 * 60 * 60)) % 24;
        int mins = (int) (uptime / (1000 * 60)) % 60;
        int secs = (int) (uptime / 1000) % 60;
        if (days > 0) {
            return String.format("Current uptime: %s days, %sh %sm %ss", days, hours, mins, secs);
        } else if (hours > 0) {
            return String.format("Current uptime: %sh %sm %ss", hours, mins, secs);
        } else {
            return String.format("Current uptime: %sm %ss", mins, secs);
        }
    }

    public static double getDimensionTPS(WorldServer worldServer) {
        double worldTickLength = mean(worldServer.getMinecraftServer().worldTickTimes.get(worldServer.provider.getDimensionId())) * 1.0E-6D;
        return Math.min(1000.0 / worldTickLength, 20);
    }

    public static double getWorldTickTime(WorldServer worldServer) {
        return mean(worldServer.getMinecraftServer().worldTickTimes.get(worldServer.provider.getDimensionId())) * 1.0E-6D;
    }

    private static long mean(long[] values) {

        long sum = 0l;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }
}