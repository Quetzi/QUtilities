package net.quetzi.qutilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraft.util.ChatComponentText;
import net.quetzi.qutilities.helpers.ChunkTools;
import net.quetzi.qutilities.helpers.MovePlayer;
import net.quetzi.qutilities.helpers.ScheduledSave;
import net.quetzi.qutilities.helpers.TeleportQueue;

public class QUtilitesEventHandler {


    private long prevTime = 0;

    @SubscribeEvent
    public void WorldTickHandler(WorldTickEvent event) {

        if (event.phase == TickEvent.Phase.END) {

            // 1200 = 1 minute
            if (QUtilities.savingEnabled && (event.world.provider.getDimensionId() == 0) && (event.world.getWorldTime() % (QUtilities.saveInterval * 1200) == 0)) {
                long currTime = event.world.getWorldTime();
                if (currTime == 0) currTime++;
                if (prevTime != currTime) {
                    ScheduledSave.saveWorldState();
                }
                prevTime = currTime;
            }
        }
        if (event.phase == TickEvent.Phase.START) {
            // Chunk pregen handler 1 chunk per tick
            if (ChunkTools.processQueue && ChunkTools.getQueueSize() > 0) {
                ChunkTools.processQueue(event.world.provider);
            }
            if (ChunkTools.getQueueSize() % 100 == 0) {
                QUtilities.log.info("Chunk pregen : " + ChunkTools.getQueueSize() + " chunks remaining");
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
