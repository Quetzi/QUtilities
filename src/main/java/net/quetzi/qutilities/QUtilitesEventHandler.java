package net.quetzi.qutilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.quetzi.qutilities.world.MovePlayer;
import net.quetzi.qutilities.world.ScheduledSave;
import net.quetzi.qutilities.world.Whitelist;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class QUtilitesEventHandler {

    @SubscribeEvent
    public void WorldTickHandler(WorldTickEvent event) {

        // 1200 = 1 minute
        if (QUtilities.savingEnabled && (event.phase == TickEvent.Phase.END) && (event.world.provider.dimensionId == 0) && (event.world.getWorldTime() % (QUtilities.saveInterval * 1200) == 0)) {
            ScheduledSave.saveWorldState();
        }
        if ((QUtilities.whitelistEnabled || QUtilities.secondaryWhitelistEnabled) && (event.phase == TickEvent.Phase.END) && (event.world.getWorldTime() % (QUtilities.checkInterval * 1200)) == 0) {
            new Thread(new Whitelist()).start();
        }
    }

    @SubscribeEvent
    public void PlayerLoggedInHandler(PlayerLoggedInEvent event) {

        MovePlayer.processQueue(event.player.getGameProfile().getName());

        if (!QUtilities.whitelistEnabled && !QUtilities.secondaryWhitelistEnabled) { return; }
        EntityPlayer player = event.player;

        if (MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) {
            QUtilities.log.info("Allowing exempt " + player.getGameProfile().getName());
            return;
        }

        if (!QUtilities.whitelist.contains(player.getGameProfile().getName().toLowerCase())) {
            QUtilities.log.info(player.getGameProfile().getName() + " not on whitelist.");
            QUtilities.log.info("Blocking " + player.getGameProfile().getName());
            ((EntityPlayerMP) player).playerNetServerHandler.kickPlayerFromServer(QUtilities.kickMessage);
        } else {
            QUtilities.log.info("Allowing " + player.getGameProfile().getName());
        }
    }
}
