package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.quetzi.qutilities.QUtilities;

import java.util.*;

/**
 * Created by Quetzi on 14/01/16.
 */
public class CommandEntity extends CommandBase {

    List<String> aliases;

    public CommandEntity() {

        aliases = new ArrayList<String>();
        aliases.add("qutil entity");
    }

    @Override
    public String getCommandName() {

        return "qent";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/qent list|killall <entityname>";
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!(args.length == 0)) {
            if (args[1].equals("list")) {
                ArrayList entities = getCumulativeEntities();
                for (Object o : entities) {
                    AmountHolder ah = (AmountHolder)o;
                    sender.addChatMessage(new ChatComponentText(ah.key + ": " + ah.value));
                }
            }
            else if (args[1].equals("killall") && args.length == 2) {
                int killed = killAll(args[2]);
                sender.addChatMessage(new ChatComponentText("Killed " + killed + " " + args[2]));
            }
        }
        else {
            sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring, BlockPos pos) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }

    /* Returns an arraylist with the entity name and amount of it on the server */
    public ArrayList<AmountHolder> getCumulativeEntities(){
        ArrayList<AmountHolder>  cumData  = new ArrayList<AmountHolder>();
        HashMap<String, Integer> entities = new HashMap<String, Integer>();

        for (int dim : DimensionManager.getIDs()){
            World world = DimensionManager.getWorld(dim);
            if (world == null) continue;

            ArrayList copyList = new ArrayList(world.loadedEntityList);

            for (Object o : copyList){
                Entity ent = (Entity)o;
                String name = ent.getClass().getName();
                //String name = getEntityName(ent, filtered);

                if (!entities.containsKey(name))
                    entities.put(name, 0);

                entities.put(name, entities.get(name) + 1);
            }
        }

        for (String key : entities.keySet())
            cumData.add(new AmountHolder(key, entities.get(key)));

//        Collections.sort(cumData);
        return cumData;
    }

    public int killAll(String entName){
        int nkilled = 0;

        if (entName.contains("Player")){
            return -1; //Error msg for when trying to kill a player
        }


        for (int dim : DimensionManager.getIDs()){
            World world = DimensionManager.getWorld(dim);
            if (world == null) continue;

            ArrayList copyList = new ArrayList(world.loadedEntityList);

            for (Object o : copyList){
                Entity ent  = (Entity)o;
                String name  = ent.getName().toLowerCase();

                if (name.equals(entName.toLowerCase())){
                    ent.setDead();
                    nkilled += 1;
                }
            }
        }

        QUtilities.log.info("Killed %d %s\n", nkilled, entName);

        return nkilled;
    }

    public class AmountHolder implements Comparable {

        public String key    = null;
        public Integer value = 0;

        public AmountHolder(String key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Object o) {

            AmountHolder ah = (AmountHolder)o;
            return Integer.compare(this.value, ah.value);
        }
    }
}