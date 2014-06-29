/*
 * @author Quetzi
 */

package net.quetzi.qutilities.world;

import net.minecraft.server.MinecraftServer;
import net.quetzi.qutilities.QUtilities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class Whitelist implements Runnable {

    public void run() {

        QUtilities.log.info("Reloading whitelist.");

        if (getRemoteWhitelist())
            QUtilities.log.info("Whitelist reloaded.");
        else
            QUtilities.log.info("Error reloading whitelist.");
    }

    public static void writeWhitelist() {

        File whitelistSave = new File(MinecraftServer.getServer().getFolderName(), "whitelist.txt");

        if (whitelistSave.exists())
            whitelistSave.delete();
        try {
            whitelistSave.createNewFile();

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

    public static boolean getRemoteWhitelist() {

        List temp = new LinkedList();

        String urlString = "http://whitelist.twitchapps.com/list.php?id=" + QUtilities.uniqueID;
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

                QUtilities.whitelist.clear();
                for (String player : (List<String>) temp)
                    QUtilities.whitelist.add(player);
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
