package net.narutoxboruto.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Clan;
import net.narutoxboruto.attachments.info.Rank;
import net.narutoxboruto.items.ModItems;
import net.narutoxboruto.util.ModUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static net.narutoxboruto.util.ModUtil.*;

public class Events {


        @SubscribeEvent
        public static void onPlayerFirstJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
                // Check if this is the first join by checking play time or use a custom flag
                if (ModUtil.getPlayerStatistics(serverPlayer, Stats.CUSTOM.get(Stats.PLAY_TIME).getValue()) == 0) {

                    // Small delay to ensure everything is loaded
                    serverPlayer.getServer().execute(() -> {
                        // Get the clan attachment and set random value
                        Clan clan = serverPlayer.getData(MainAttachment.CLAN);
                        clan.setValue(getRandomIndex(CLAN_LIST), serverPlayer);

                        // Get rank attachment and set value
                        Rank rank = serverPlayer.getData(MainAttachment.RANK);
                        rank.setValue("student", serverPlayer);

                        serverPlayer.getData(MainAttachment.AFFILIATION).setValue(getRandomIndex(AFF_LIST), serverPlayer);

                        giveClanStatBonuses(serverPlayer);
                        serverPlayer.addItem(new ItemStack(ModItems.CHAKRA_PAPER.get()));

                        // Debug output
                        System.out.println("Set rank to: " + rank.getValue());
                    });
                }
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
