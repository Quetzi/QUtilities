/*
 * @author Quetzi
 */

package net.quetzi.qutilities.world;

import net.minecraft.server.MinecraftServer;
import net.quetzi.qutilities.QUtilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Whitelist implements Runnable {

    public void run() {

        Thread.currentThread().setName("QUtilities-Whitelist");
        while (!Thread.currentThread().isInterrupted()) {
            QUtilities.log.info("Reloading whitelist.");
            if (updateWhitelist()) QUtilities.log.info("Whitelist reloaded.");
            else QUtilities.log.info("Error reloading whitelist.");
            Thread.currentThread().interrupt();
        }
    }

    public static void writeWhitelist() {

        File whitelistSave = new File(MinecraftServer.getServer().getFolderName(), "whitelist-export.txt");

        if (whitelistSave.exists()) whitelistSave.delete();
        try {
            if (!whitelistSave.createNewFile()) {
                QUtilities.log.info(("Error saving whitelist"));
            }
            FileWriter fstream = new FileWriter(whitelistSave);
            BufferedWriter out = new BufferedWriter(fstream);

            for (String player : QUtilities.whitelist) {
                out.write(player + "\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean updateWhitelist() {

        boolean whitelist1 = false;
        boolean whitelist2 = false;

        QUtilities.whitelist.clear();
        if (QUtilities.whitelistEnabled && QUtilities.secondaryWhitelistEnabled) {
            return getRemoteWhitelist("http://whitelist.twitchapps.com/list.php?id=" + QUtilities.uniqueID) && getRemoteWhitelist(QUtilities.secondaryWhitelistLocation);
        }
        else if (QUtilities.whitelistEnabled) {
            return getRemoteWhitelist("http://whitelist.twitchapps.com/list.php?id=" + QUtilities.uniqueID);
        }
        else if (QUtilities.secondaryWhitelistEnabled) {
            return getRemoteWhitelist(QUtilities.secondaryWhitelistLocation);
        }
        return false;
    }

    public static boolean getRemoteWhitelist(String urlString) {

        try {
            QUtilities.log.info("Getting whitelist from " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    inputLine = inputLine.trim();
                    QUtilities.whitelist.add(inputLine.toLowerCase());
                }
                in.close();
            } catch (IOException e) {
                String errorIn = "";
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    BufferedReader inE = new BufferedReader(new InputStreamReader(errorStream));
                    String inputLine;
                    while ((inputLine = inE.readLine()) != null)
                        errorIn = errorIn + inputLine;
                    inE.close();
                }
                QUtilities.log.info("Error getting list: " + errorIn);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
