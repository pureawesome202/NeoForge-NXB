package net.narutoxboruto.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.attachments.info.MaxChakra;
import net.narutoxboruto.attachments.modes.ChakraControl;
import net.narutoxboruto.attachments.stats.Kenjutsu;
import net.narutoxboruto.attachments.stats.Speed;
import net.narutoxboruto.attachments.stats.Taijutsu;
import net.narutoxboruto.effect.ModEffects;
import net.narutoxboruto.util.ModUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatEvents {

    private static final Map<UUID, Integer> playerHitCounters = new HashMap<>();
    private static boolean taiFlag, kenFlag;
    private static int chakraDrainTimer;

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        // Check if there's an attacker and it's a player
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            UUID playerId = serverPlayer.getUUID();

            // Increment hit counter
            int currentHits = playerHitCounters.getOrDefault(playerId, 0) + 1;
            playerHitCounters.put(playerId, currentHits);

            // Every 20 hits, give combat points
            if (currentHits >= 20) {
                if (serverPlayer.getMainHandItem().isEmpty()) {
                    Taijutsu taijutsu = serverPlayer.getData(MainAttachment.TAIJUTSU);
                    taijutsu.incrementValue(1, serverPlayer);
                    taiFlag = true;
                }
                else if (serverPlayer.getMainHandItem().getItem() instanceof SwordItem) {
                    Kenjutsu kenjutsu = serverPlayer.getData(MainAttachment.KENJUTSU);
                    kenjutsu.incrementValue(1, serverPlayer);
                    kenFlag = true;
                }

                // Reset counter
                playerHitCounters.put(playerId, 0);
            }
        }
    }

    @SubscribeEvent
    public static void addStatBonuses(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Speed speed = serverPlayer.getData(MainAttachment.SPEED);
            int speedLevel = speed.getValue() / 10; // Adjust this divisor based on your desired progression

            // Only apply effect if we have a positive level
            if (speedLevel > 0) {
                // Get current speed effect if it exists
                MobEffectInstance currentEffect = serverPlayer.getEffect(MobEffects.MOVEMENT_SPEED);

                // Only apply new effect if the level changed or effect doesn't exist
                if (currentEffect == null || currentEffect.getAmplifier() != speedLevel - 1) {
                    // Remove old effect first
                    serverPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);

                    // Apply new effect with proper duration and settings
                    serverPlayer.addEffect(
                            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, speedLevel - 1,
                                    false, false, true)); // Added ambient:true for persistent effect
                }
            } else if (speedLevel == 0) {
                // Remove speed effect if we don't qualify for it anymore
                serverPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
            }
        }
    }

    @SubscribeEvent
    public static void tickStats(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            // Chakra regeneration - improved with better timing
            if (serverPlayer.tickCount % 6000 == 0) { // Check every 5 min instead of random
                Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);
                MaxChakra maxChakra = serverPlayer.getData(MainAttachment.MAX_CHAKRA);

                if (chakra.getValue() < maxChakra.getValue()) {
                    // Regenerate based on missing chakra (faster when lower)
                    int missingChakra = maxChakra.getValue() - chakra.getValue();
                    int regenAmount = Math.max(1, missingChakra / 5); // Regenerate 5% of missing chakra
                    chakra.addValue(regenAmount, serverPlayer);
                }
            }

            if (serverPlayer.tickCount % 100 == 0) { // Check every 5 seconds to reduce lag
                Speed speed = serverPlayer.getData(MainAttachment.SPEED);

                int distanceSprinted = ModUtil.getPlayerStatistics(serverPlayer, Stats.SPRINT_ONE_CM) / 100;

                int targetSpeedValue = distanceSprinted / 150;

                int currentSpeedValue = speed.getValue();

                // Only increase speed, never decrease
                if (targetSpeedValue > currentSpeedValue) {
                    speed.setValue(targetSpeedValue, serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void addStatExtraDamage(LivingDamageEvent.Post event) {
        LivingEntity entity = event.getEntity();
        if (!entity.level().isClientSide() && entity.getLastAttacker() instanceof ServerPlayer serverPlayer) {
            if (taiFlag) {
                Taijutsu taijutsu = serverPlayer.getData(MainAttachment.TAIJUTSU);
                entity.hurt(entity.damageSources().generic(), (float) taijutsu.getValue() / 33.33F);
                taiFlag = false;
            }
            else if (kenFlag) {
                Kenjutsu kenjutsu = serverPlayer.getData(MainAttachment.KENJUTSU);
                entity.hurt(entity.damageSources().generic(), (float) kenjutsu.getValue() / 33.33F);
                kenFlag = false;
            }
        }
    }

    @SubscribeEvent
    public static void chakraControl(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer)  {
            ChakraControl chakraControl = serverPlayer.getData(MainAttachment.CHAKRA_CONTROL);
            if (serverPlayer.hasEffect(ModEffects.CHAKRA_CONTROL)) {
                Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);
                if (chakra.getValue() > 0) {
                    if (chakraDrainTimer > 0) {
                        chakraDrainTimer--;
                    }
                    else {
                        chakraDrainTimer = 600;
                        chakra.subValue(1, serverPlayer);
                    }
                }
                else {
                    chakraControl.setValue(false, serverPlayer);
                    chakraDrainTimer = 0;
                    serverPlayer.displayClientMessage(Component.translatable("msg.no_chakra"), true);
                }
            }
        }
    }
}
