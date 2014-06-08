package net.quetzi.qutilities;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.qutilities.world.MovePlayer;
import net.quetzi.qutilities.world.ScheduledSave;

public class QUtilitesEventHandler
{
    @SubscribeEvent
    public void WorldTickHandler(WorldTickEvent event)
    {
        // 1200 = 1 minute
        if (QUtilities.savingEnabled && (event.phase == TickEvent.Phase.END) && (event.world.provider.dimensionId == 0) && (event.world.getWorldTime() % (QUtilities.saveInterval * 1200) == 0)) {
            ScheduledSave.saveWorldState();
        }
    }

    @SubscribeEvent
    public void PlayerLoggedInHandler(PlayerLoggedInEvent event)
    {
        MovePlayer.processQueue(event.player.getGameProfile().getName());
    }
}
