package net.narutoxboruto.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.narutoxboruto.attachments.info.Affiliation;
import net.narutoxboruto.attachments.info.Clan;
import net.narutoxboruto.attachments.info.Rank;
import net.narutoxboruto.items.ModItems;
import net.narutoxboruto.util.ModUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static net.narutoxboruto.attachments.MainAttachment.*;
import static net.narutoxboruto.attachments.MainAttachment.RANK;
import static net.narutoxboruto.util.ModUtil.*;
import static net.narutoxboruto.util.ModUtil.getRandomIndex;

public class Events {


    @SubscribeEvent
    public static void onPlayerFirstJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer
                && ModUtil.getPlayerStatistics(serverPlayer, Stats.PLAY_TIME) == 0) {

            Clan clan = serverPlayer.getData(CLAN);
            Affiliation affiliation = serverPlayer.getData(AFFILIATION);
            Rank rank = serverPlayer.getData(RANK);

            clan.setValue(getRandomIndex(CLAN_LIST));
            affiliation.setValue(getRandomIndex(AFF_LIST));
            rank.setValue("student");

            clan.syncValue(serverPlayer);
            affiliation.syncValue(serverPlayer);
            rank.syncValue(serverPlayer);

            giveClanStatBonuses(serverPlayer);
            serverPlayer.addItem(new ItemStack(ModItems.CHAKRA_PAPER.get()));
            // Send packet to open the GUI
            // Open the GUI
            //ModPacketHandler.INSTANCE2.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncStartGui());
        }

    }

       // @SubscribeEvent
       // public static void onProjectileImpact(ProjectileImpactEvent event) {
       //     Level level = event.getProjectile().level;
       //     BlockPos pos = new BlockPos(event.getRayTraceResult().getLocation());
       //     Block hitBlock = level.getBlockState(pos).getBlock();
       //     if (!level.isClientSide && hitBlock instanceof PaperBombBlock) {
       //         PaperBombBlock.explode(level, pos);
       //     }
       // }

}
