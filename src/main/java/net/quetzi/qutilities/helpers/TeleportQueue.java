package net.quetzi.qutilities.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quetzi on 06/11/14.
 */
public class TeleportQueue {


    private List<TeleportEntry> queue = new ArrayList<TeleportEntry>();

    public TeleportQueue() {

    }
    public class TeleportEntry {

        private String player;
        private String type;
        private int dim;
        private double x;
        private double y;
        private double z;

        public TeleportEntry(String player, int dim, double x, double y, double z) {

            this.player = player;
            this.type = "location";
            this.dim = dim;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public TeleportEntry(String player) {

            this.player = player;
            this.type = "default";
        }

        public String getPlayer() {
            return player;
        }

        public int getDim() {
            return dim;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }

    public boolean addToQueue(String player) {
        return this.queue.add(new TeleportEntry(player));
    }

    public boolean addToQueue(String player, int dim, double x, double y, double z) {

        return this.queue.add(new TeleportEntry(player.toLowerCase(), dim, x, y, z));
    }

    public boolean process(String player) {
        for (TeleportEntry te : this.queue) {
            if (te.getPlayer().equals(player.toLowerCase())) {
                if (te.type.equals("default")) {
                    MovePlayer.sendToDefaultSpawn(te.getPlayer());
                } else {
                    MovePlayer.sendToLocation(player, te.getDim(), te.getX(), te.getY(), te.getZ());
                }
                remove(player);
                return true;
            }
        }
        return false;
    }

    public void remove(String player) {

        for (TeleportEntry te : this.queue) {
            if (te.getPlayer().equals(player.toLowerCase())) {
                this.queue.remove(te);
            }
        }
    }

    public boolean isQueued(String player) {

        for (TeleportEntry te : this.queue) {
            if (te.getPlayer().equals(player.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public List<String> getQueue() {

        List<String> queuedPlayers = new ArrayList<String>();
        for (TeleportEntry te : this.queue) {
            queuedPlayers.add(te.getPlayer());
        }
        return queuedPlayers;
    }
}
