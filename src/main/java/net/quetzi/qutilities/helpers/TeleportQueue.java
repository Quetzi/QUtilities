package net.quetzi.qutilities.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Quetzi on 06/11/14.
 */
public class TeleportQueue {


    private Set<TeleportEntry> queue = new HashSet<TeleportEntry>();

    public TeleportQueue() {

    }
    public class TeleportEntry {

        private String player;
        private int dim;
        private int x;
        private int y;
        private int z;
        public TeleportEntry(String player, int dim, int x, int y, int z) {

            this.player = player;
            this.dim = dim;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public String getPlayer() {
            return player;
        }

        public int getDim() {
            return dim;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }

    public void add(String player, int dim, int x, int y, int z) {
        this.queue.add(new TeleportEntry(player, dim, x, y, z));
    }

    public boolean process(String player) {
        for (TeleportEntry te : this.queue) {
            if (te.getPlayer().equals(player.toLowerCase())) {
                MovePlayer.sendToLocation(player, te.getDim(), te.getX(), te.getY(), te.getZ());
                remove(player);
                return true;
            }
        }
        return false;
    }

    public void remove(String player) {
        for (TeleportEntry te : this.queue) {
            if (te.getPlayer().equals(player.toLowerCase())) {
                queue.remove(te);
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
