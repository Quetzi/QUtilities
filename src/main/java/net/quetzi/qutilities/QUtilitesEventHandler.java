package net.quetzi.qutilities;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.ScheduledSave;

public class QUtilitesEventHandler {

    private long prevTime = 0;

    @SubscribeEvent
    public void WorldTickHandler(WorldTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {

            // 1200 = 1 minute
            if (QUtilities.savingEnabled && (event.world.provider.dimensionId == 0) && (event.world.getWorldTime() % (QUtilities.saveInterval * 1200) == 0)) {
                long currTime = event.world.getWorldTime();
                if (currTime == 0) currTime++;
                if (prevTime != currTime) {
                    ScheduledSave.saveWorldState();
                }
                prevTime = currTime;
            }
        }
    }

    @SubscribeEvent
    public void PlayerLoggedInHandler(PlayerLoggedInEvent event) {

        if (QUtilities.queue.process(event.player.getGameProfile().getName().toLowerCase())) {
            QUtilities.log.info(event.player.getGameProfile().getName() + " was queued to move and has been moved");
        }
        if (QUtilities.enableMotd) {
            event.player.addChatComponentMessage(new ChatComponentText(QUtilities.motd));
        }
    }
}
