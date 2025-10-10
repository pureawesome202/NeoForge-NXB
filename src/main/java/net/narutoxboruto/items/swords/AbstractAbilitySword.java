package net.narutoxboruto.items.swords;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.narutoxboruto.capabilities.info.Chakra;
import net.narutoxboruto.networking.ModPacketHandler;

public class AbstractAbilitySword extends SwordItem implements Vanishable {
   public boolean isActive = false;
   protected int cooldown;

    public AbstractAbilitySword(float pAttackSpeedModifier, Item.Properties pProperties) {
        super(SwordCustomTiers.GENERAL, pProperties);
        this.cooldown = (int) (20 / (4 + pAttackSpeedModifier));
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

   protected void doSpecialAbility(ServerPlayer serverPlayer){
   }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Damage the sword
        stack.hurtAndBreak(1, attacker, (EquipmentSlot.MAINHAND));

        if (attacker instanceof ServerPlayer serverPlayer && this.isActive && !serverPlayer.level().isClientSide) {
            ItemCooldowns cooldowns = serverPlayer.getCooldowns();

            // Access Chakra attachment directly
            Chakra chakra = serverPlayer.getData(ModPacketHandler.CHAKRA);

            if (!cooldowns.isOnCooldown(stack.getItem()) && chakra.getValue() >= getChakraCost()) {
                doSpecialAbility(target, serverPlayer);
                chakra.subValue(getChakraCost(), serverPlayer);
            }

            cooldowns.addCooldown(this, cooldown);
        }
        return true;
    }
}
