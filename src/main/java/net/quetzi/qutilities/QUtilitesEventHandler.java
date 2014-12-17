package net.quetzi.qutilities;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.ChunkTools;
import net.quetzi.qutilities.helpers.MovePlayer;
import net.quetzi.qutilities.helpers.ScheduledSave;
import net.quetzi.qutilities.helpers.TeleportQueue;

public class QUtilitesEventHandler {


    private long prevTime = 0;
    private long prevWLTime = 0;

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

        TeleportQueue tq = TeleportQueue.process(event.player.getGameProfile().getName());
        if (tq != null) {
            MovePlayer.sendToLocation(tq.getPlayer(), tq.getDim(), tq.getX(), tq.getY(), tq.getZ());
            TeleportQueue.queue.remove(tq);
        }
        event.player.addChatComponentMessage(new ChatComponentText(QUtilities.motd));

        if (ChunkTools.processQueue) {
            ((EntityPlayerMP)event.player).playerNetServerHandler.kickPlayerFromServer("Server is currently pregenerating chunks, please try again later");
        }
    }
}
