package net.narutoxboruto.items.scrolls;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.Senjutsu;
import net.narutoxboruto.attachments.stats.Speed;

public class SenjutsuScroll extends Item {

    public SenjutsuScroll(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide() && pPlayer instanceof ServerPlayer serverPlayer) {
            Senjutsu senjutsu = serverPlayer.getData(MainAttachment.SENJUTSU);
            senjutsu.incrementValue(10, serverPlayer);
            serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(10, serverPlayer);

            // Consume the item
            if (!pPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Trigger advancement and stats
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            pPlayer.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }
}
