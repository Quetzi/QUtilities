package net.quetzi.qutilities;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.qutilities.helpers.ScheduledSave;

public class QUtilitesEventHandler {

    private long prevTime = 0;

    @SubscribeEvent
    public void WorldTickHandler(WorldTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {

            // 1200 = 1 minute
            if (QUtilities.savingEnabled && (event.world.provider.getDimension() == 0) && (event.world.getWorldTime() % (QUtilities.saveInterval * 1200) == 0)) {
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
            event.player.addChatComponentMessage(new TextComponentString(QUtilities.motd));
        }
    }
}
