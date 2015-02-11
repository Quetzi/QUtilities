package net.quetzi.qutilities.helpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Quetzi on 06/11/14.
 */
public class TeleportQueue {

    private String player;
    private int dim;
    private int x;
    private int y;
    private int z;
    public static Set<TeleportQueue> queue = new HashSet<TeleportQueue>();

    public TeleportQueue(String player, int dim, int x, int y, int z) {

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

    public static void add(String player, int dim, int x, int y, int z) {
        queue.add(new TeleportQueue(player, dim, x, y, z));
    }

    public static boolean process(String player) {
        for (TeleportQueue tq : queue) {
            if (tq.getPlayer().equals(player.toLowerCase())) {
                MovePlayer.sendToLocation(player, tq.getDim(), tq.getX(), tq.getY(), tq.getZ());
                remove(player);
                return true;
            }
        }
        return false;
    }

    public static void remove(String player) {
        for (TeleportQueue tq : queue) {
            if (tq.getPlayer().equals(player.toLowerCase())) {
                queue.remove(tq);
            }
        }
    }

    public static boolean isQueued(String player) {
        for (TeleportQueue tq : queue) {
            if (tq.getPlayer().equals(player.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getQueue() {

        List<String> queuedPlayers = new ArrayList<String>();
        for (TeleportQueue tq : queue) {
            queuedPlayers.add(tq.getPlayer());
        }
        return queuedPlayers;
    }
}
