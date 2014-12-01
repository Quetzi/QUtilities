package net.quetzi.qutilities.helpers;

/**
 * Created by Quetzi on 01/12/14.
 */
import net.quetzi.qutilities.QUtilities;

import java.text.NumberFormat;

public class SystemInfo {

    private static Runtime runtime = Runtime.getRuntime();
    private static NumberFormat format = NumberFormat.getInstance();

    public static String Info() {
        StringBuilder sb = new StringBuilder();
        sb.append(OsInfo());
        sb.append(MemInfo());
        return sb.toString();
    }

    public static String OSname() {
        return System.getProperty("os.name");
    }

    public static String OSversion() {
        return System.getProperty("os.version");
    }

    public static String OsArch() {
        return System.getProperty("os.arch");
    }

    public static long getTotalMem() {
        return runtime.totalMemory();
    }

    public static long getUsedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
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

    public static String MemInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append("/n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append("/n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append("/n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        return sb.toString();

    }

    public static String OsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("OS: ");
        sb.append(OSname());
        sb.append("/n");
        sb.append("Version: ");
        sb.append(OSversion());
        sb.append("/n");
        sb.append(": ");
        sb.append(OsArch());
        sb.append("/n");
        sb.append("Available processors (cores): ");
        sb.append(runtime.availableProcessors());
        sb.append("/n");
        return sb.toString();
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
}