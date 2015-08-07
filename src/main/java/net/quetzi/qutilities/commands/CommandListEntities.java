package net.quetzi.qutilities.commands;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;

import java.util.*;

/**
 * Created by Quetzi on 30/07/15.
 */
public class CommandListEntities extends CommandBase {

    @Override
    public String getCommandName() {

        return "qentlist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "qentlist";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        EntityList ents = getEntityCounts();
        Iterator ite = ents.list.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)ite.next();
            sender.addChatMessage(new ChatComponentText(entry.getKey() + ": " + entry.getValue()));
        }
    }

    private ArrayList getEntityList() {

        ArrayList<Entity> entities = new ArrayList();
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            entities.addAll((ArrayList<Entity>) MinecraftServer.getServer().worldServers[world.provider.dimensionId].loadedEntityList);
        }
        return entities;
    }

    private EntityList getEntityCounts() {

        ArrayList<Entity> ents = getEntityList();
        EntityList entCount = new EntityList();

        for (Entity ent : ents) {
            entCount.add(ent.getCommandSenderName());
        }
        return entCount;
    }

    private class EntityList {

        public HashMap<String, Integer> list = new HashMap<String, Integer>();

        public EntityList() {
        }

        public void add(String name) {

            if (list.containsKey(name)) {
                list.put(name, list.get(name)+1);
            } else {
                list.put(name, 0);
            }
        }
    }
}
