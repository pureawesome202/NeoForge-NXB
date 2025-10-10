package net.narutoxboruto.items.swords;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EquipmentSlot;
import net.narutoxboruto.capabilities.info.Chakra;
import net.narutoxboruto.capabilities.info.MaxChakra;
import net.narutoxboruto.networking.ModPacketHandler;

import javax.annotation.Nullable;
import java.util.List;

public class Samehada extends AbstractAbilitySword {
    private static final int MAX_SWORD_CHAKRA = 250;
    private int chakraDiff;
    private int swordChakra;

    public Samehada(int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pAttackDamageModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.literal("Req: 300 Ken").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, context, components, flag);
    }

    @Override
    public int getChakraCost() {
        return 0;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            doSpecialAbility(player, serverPlayer);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    /**
     * Calculates how much chakra can be absorbed.
     */
    public void abilityChakra(LivingEntity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            Chakra chakra = serverPlayer.getData(ModPacketHandler.CHAKRA);
            MaxChakra maxChakra = serverPlayer.getData(ModPacketHandler.MAX_CHAKRA);
            this.chakraDiff = maxChakra.getValue() - chakra.getValue();
        }
    }

    @Override
    protected void doSpecialAbility(LivingEntity target, ServerPlayer serverPlayer) {
        abilityChakra(target);
        if (target instanceof ServerPlayer && swordChakra > 0) {
            Chakra chakra = serverPlayer.getData(ModPacketHandler.CHAKRA);
            chakra.addValue(swordChakra, serverPlayer.getData(ModPacketHandler.MAX_CHAKRA).getValue(), serverPlayer);
            swordChakra = Math.max(swordChakra - this.chakraDiff, 0);
        }
        serverPlayer.sendSystemMessage(Component.translatable("samehada.chakra_amount", swordChakra));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof ServerPlayer serverPlayer) {
            Chakra chakra = serverPlayer.getData(ModPacketHandler.CHAKRA);
            if (swordChakra >= MAX_SWORD_CHAKRA) {
                chakra.subValue(3, serverPlayer);
            }
            swordChakra = Math.min(swordChakra + 3, MAX_SWORD_CHAKRA);
        }
        swordChakra = Math.min(swordChakra + 3, MAX_SWORD_CHAKRA);
        stack.hurtAndBreak(1, attacker, (EquipmentSlot.MAINHAND));
        return true;
    }
}
