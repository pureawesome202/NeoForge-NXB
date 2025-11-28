package net.narutoxboruto.items.swords;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;

public class AbstractAbilitySword extends SwordItem implements Vanishable {
   public boolean isActive = false;
   protected int cooldown;

    public AbstractAbilitySword(Tier tier, Item.Properties pProperties) {
        // The parent SwordItem constructor now only needs the tier and properties
        super(tier, pProperties);
        // You can calculate cooldown based on the tier's speed if needed
        this.cooldown = (int) (20 / (4 + tier.getSpeed()));
    }

    public int getChakraCost() {
        return 1;
    }

    public void toggleAbility(Player pPlayer) {
        if (!pPlayer.level().isClientSide) {
            this.isActive = !this.isActive;
            String s = this.isActive ? "activate" : "deactivate";
            pPlayer.displayClientMessage(Component.translatable("sword_ability." + s, this.getDescription()), true);
        }
    }

    protected void doSpecialAbility(LivingEntity pTarget, ServerPlayer serverPlayer) {
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, EquipmentSlot.MAINHAND);

        if (pAttacker instanceof ServerPlayer serverPlayer && this.isActive && pAttacker.level() instanceof ServerLevel) {
            ItemCooldowns cooldowns = serverPlayer.getCooldowns();

            //Get the chakra attachment from the player
            Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);

            if (!cooldowns.isOnCooldown(pStack.getItem()) && chakra.getValue() >= getChakraCost()) {
                doSpecialAbility(pTarget, serverPlayer);
                chakra.subValue(getChakraCost(), serverPlayer);
                //Sync the updated data to the client
                serverPlayer.syncData(MainAttachment.CHAKRA);
                cooldowns.addCooldown(this, cooldown);
            }
        }
        return true;
    }

    protected boolean consumeChakra(ServerPlayer serverPlayer) {
        Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);
        if (chakra.getValue() >= getChakraCost()) {
            chakra.subValue(getChakraCost(), serverPlayer);
            serverPlayer.syncData(MainAttachment.CHAKRA);
            return true;
        }
        return false;
    }

}
