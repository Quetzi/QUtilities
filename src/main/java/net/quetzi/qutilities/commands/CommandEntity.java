package net.quetzi.qutilities.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.quetzi.qutilities.QUtilities;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Quetzi on 14/01/16.
 */
public class CommandEntity extends CommandBase
{
    private List<String> aliases;

    public CommandEntity()
    {
        aliases = new ArrayList<>();
    }

    @Nonnull
    @Override
    public String getCommandName()
    {
        return "qent";
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender sender)
    {
        return "/qent list|killall <entityname>";
    }

    @Nonnull
    @Override
    public List<String> getCommandAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args)
    {
        if (!(args.length == 0))
        {
            if (args[0].equals("list"))
            {
                ArrayList entities = getCumulativeEntities();
                for (Object o : entities)
                {
                    AmountHolder ah = (AmountHolder) o;
                    sender.addChatMessage(new TextComponentString(ah.key + ": " + ah.value));
                }
            }
            else if (args[0].equals("killall") && args.length == 2)
            {
                int killed = killAll(args[1]);
                sender.addChatMessage(new TextComponentString("Killed " + killed + " " + args[1]));
            }
        }
        else
        {
            sender.addChatMessage(new TextComponentString(getCommandUsage(sender)));
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return new ArrayList<>();
    }

    /* Returns an arraylist with the entity name and amount of it on the server */
    private ArrayList<AmountHolder> getCumulativeEntities()
    {
        ArrayList<AmountHolder> cumData = new ArrayList<>();
        HashMap<String, Integer> entities = new HashMap<>();

        for (int dim : DimensionManager.getIDs())
        {
            World world = DimensionManager.getWorld(dim);
            if (world == null) continue;

            List<Entity> copyList = world.loadedEntityList;

            for (Entity ent : copyList)
            {
                String name = ent.getDisplayName().getUnformattedText();
                //String name = getEntityName(ent, filtered);

                if (!entities.containsKey(name))
                {
                    entities.put(name, 0);
                }

                entities.put(name, entities.get(name) + 1);
            }
        }
        cumData.addAll(entities.keySet().stream().map(key -> new AmountHolder(key, entities.get(key))).collect(Collectors.toList()));

        Collections.sort(cumData);
        return cumData;
    }

    private int killAll(String entName)
    {
        int nkilled = 0;

        if (entName.contains("Player"))
        {
            return -1; //Error msg for when trying to kill a player
        }
        for (int dim : DimensionManager.getIDs())
        {
            World world = DimensionManager.getWorld(dim);
            if (world == null) continue;

            List<Entity> copyList = world.loadedEntityList;

            for (Entity ent : copyList)
            {
                String name = ent.getDisplayName().getUnformattedText().toLowerCase();

                if (name.contains(entName.toLowerCase()))
                {
                    ent.setDead();
                    nkilled += 1;
                }
            }
        }
        return nkilled;
    }

    private class AmountHolder implements Comparable
    {
        String  key   = null;
        Integer value = 0;

        AmountHolder(String key, int value)
        {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(@Nonnull Object o)
        {
            AmountHolder ah = (AmountHolder) o;
            return Integer.compare(this.value, ah.value);
        }
    }
}
