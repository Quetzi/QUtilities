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
import java.util.LinkedList;
import java.util.List;

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

        QUtilities.whitelist.clear();
        if (QUtilities.whitelistEnabled) {
            getRemoteWhitelist("http://whitelist.twitchapps.com/list.php?id=" + QUtilities.uniqueID);
        }
        if (QUtilities.secondaryWhitelistEnabled) {
            getRemoteWhitelist(QUtilities.secondaryWhitelistLocation);
        }
        return true;
    }

    private static void addToWhitelist(List<String> playerList) {
        for (String player : playerList) {
            QUtilities.whitelist.add(player);
        }
    }
    public static boolean getRemoteWhitelist(String urlString) {

        List<String> temp = new LinkedList<String>();

        try {
            QUtilities.log.info("Getting whitelist from " + urlString);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    inputLine = inputLine.trim();
                    temp.add(inputLine.toLowerCase());
                }
                in.close();
                addToWhitelist(temp);
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
