package net.narutoxboruto.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.attachments.info.MaxChakra;
import net.narutoxboruto.attachments.modes.ChakraControl;
import net.narutoxboruto.attachments.stats.Kenjutsu;
import net.narutoxboruto.attachments.stats.Medical;
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
    private static final Map<UUID, Integer> playerDamageCounters = new HashMap<>(); // New counter for tracking times player gets hit
    private static boolean taiFlag, kenFlag;
    private static int chakraDrainTimer;

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            UUID playerId = serverPlayer.getUUID();

            // Increment hit counter
            int currentHits = playerHitCounters.getOrDefault(playerId, 0) + 1;
            playerHitCounters.put(playerId, currentHits);

            // Every 20 hits, give combat point
            if (currentHits >= 20) {
                if (serverPlayer.getMainHandItem().isEmpty()) {
                    Taijutsu taijutsu = serverPlayer.getData(MainAttachment.TAIJUTSU);
                    taijutsu.incrementValue(1, serverPlayer);
                    // Award SP for actual stat gain
                    serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(1, serverPlayer);
                    taiFlag = true;
                }
                else if (serverPlayer.getMainHandItem().getItem() instanceof SwordItem) {
                    Kenjutsu kenjutsu = serverPlayer.getData(MainAttachment.KENJUTSU);
                    kenjutsu.incrementValue(1, serverPlayer);
                    // Award SP for actual stat gain
                    serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(1, serverPlayer);
                    kenFlag = true;
                }

                // Reset counter
                playerHitCounters.put(playerId, 0);
            }
        }

        // NEW: Check if the entity taking damage is a player (for receiving damage)
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            UUID playerId = serverPlayer.getUUID();

            // Increment damage taken counter
            int currentDamageTaken = playerDamageCounters.getOrDefault(playerId, 0) + 1;
            playerDamageCounters.put(playerId, currentDamageTaken);

            // Every 20 times the player gets hit, give extra max HP
            if (currentDamageTaken >= 20) {
                // Get medical attachment and increment it
                Medical medical = serverPlayer.getData(MainAttachment.MEDICAL);
                medical.incrementValue(1, serverPlayer);
                // Award SP for actual stat gain
                serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(1, serverPlayer);

                // Increase max health by 1 heart (2 HP)
                AttributeInstance maxHealthAttr = serverPlayer.getAttribute(Attributes.MAX_HEALTH);
                if (maxHealthAttr != null) {
                    double currentMaxHealth = maxHealthAttr.getBaseValue();
                    maxHealthAttr.setBaseValue(currentMaxHealth + 2.0); // Add 1 heart
                }
                // Reset counter
                playerDamageCounters.put(playerId, 0);
            }
        }
    }

    @SubscribeEvent
    public static void addStatBonuses(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Speed speed = serverPlayer.getData(MainAttachment.SPEED);
            int speedLevel = speed.getValue() / 10;

            // Only apply effect if we have a positive level
            if (speedLevel > 0) {
                MobEffectInstance currentEffect = serverPlayer.getEffect(MobEffects.MOVEMENT_SPEED);

                if (currentEffect == null || currentEffect.getAmplifier() != speedLevel - 1) {
                    serverPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
                    serverPlayer.addEffect(
                            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, -1, speedLevel - 1,
                                    false, false, true));
                }
            } else if (speedLevel == 0) {
                serverPlayer.removeEffect(MobEffects.MOVEMENT_SPEED);
            }
        }
    }

    @SubscribeEvent
    public static void tickStats(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.tickCount % 6000 == 0) {
                Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);
                MaxChakra maxChakra = serverPlayer.getData(MainAttachment.MAX_CHAKRA);

                if (chakra.getValue() < maxChakra.getValue()) {
                    int missingChakra = maxChakra.getValue() - chakra.getValue();
                    int regenAmount = Math.max(1, missingChakra / 5);
                    chakra.addValue(regenAmount, serverPlayer);
                }
            }

            if (serverPlayer.tickCount % 100 == 0) {
                Speed speed = serverPlayer.getData(MainAttachment.SPEED);
                int distanceSprinted = ModUtil.getPlayerStatistics(serverPlayer, Stats.SPRINT_ONE_CM) / 100;
                int targetSpeedValue = distanceSprinted / 150;
                int currentSpeedValue = speed.getValue();

                if (targetSpeedValue > currentSpeedValue) {
                    speed.setValue(targetSpeedValue, serverPlayer);
                    serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(serverPlayer);
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
