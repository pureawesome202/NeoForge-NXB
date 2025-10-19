package net.narutoxboruto.events;

import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.main.Main;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


@Mod(Main.MOD_ID)
public class AttachmentEvents {

    public static void onReplenishChakra(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.isSleepingLongEnough()) {
            Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA.get());
            chakra.replenish(serverPlayer);
        }
    }
}
