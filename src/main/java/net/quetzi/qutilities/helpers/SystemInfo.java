package net.quetzi.qutilities.helpers;

/**
 * Created by Quetzi on 01/12/14.
 */

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
        return StringFormatter.bytesToString(runtime.totalMemory());
    }

    public static String getUsedMem() {
        return StringFormatter.bytesToString(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public static String getFreeMem() {
        return StringFormatter.bytesToString(runtime.freeMemory());
    }

    public static String getAllocatedMem() {
        return StringFormatter.bytesToString(runtime.totalMemory());
    }

    public static String getMaxMem() {
        return StringFormatter.bytesToString(runtime.maxMemory());
    }

    public static String getPercentMemUse() {
        return format.format(((float)runtime.totalMemory() / (float)runtime.maxMemory()) * 100);
    }

    public static String getUptime() {

        long uptime = System.currentTimeMillis() - QUtilities.startTime;
        return StringFormatter.millisToString(uptime);
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