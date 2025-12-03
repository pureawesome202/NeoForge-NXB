package net.narutoxboruto.items.swords;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.narutoxboruto.items.jutsus.EarthWave.*;
import static net.narutoxboruto.util.BlockUtils.isBlockEntity;

public class Kabutowari extends AbstractAbilitySword {

    public Kabutowari(Properties pProperties) {
        super(SwordCustomTiers.NUIBARI, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (pLevel.isClientSide()) {
            return InteractionResultHolder.consume(itemStack);
        }

        if (pPlayer instanceof ServerPlayer serverPlayer) {
            doSpecialAbility(pPlayer, serverPlayer);
            return InteractionResultHolder.success(itemStack);
        }

        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    protected void doSpecialAbility(LivingEntity pTarget, ServerPlayer serverPlayer) {
        Level level = serverPlayer.level();

        if (level.isClientSide) {
            return;
        }

        BlockPos playerPos = serverPlayer.blockPosition();
        int playerX = playerPos.getX();
        int playerY = playerPos.getY();
        int playerZ = playerPos.getZ();

        int maxDistance = 5;
        int waveCount = 5;
        int currentTick = serverPlayer.getServer().getTickCount();

        // Create expanding waves with increasing delays
        for (int wave = 1; wave <= waveCount; wave++) {
            final int waveNumber = wave;
            final int waveRadius = waveNumber; // Each wave goes 1 block further

            int delayTicks = (waveNumber - 1) * 15; // 0, 15, 30, 45, 60 ticks (0.75 sec between waves)

            serverPlayer.getServer().tell(
                    new TickTask(
                            currentTick + delayTicks,
                            () -> {
                                // Create a circle of blocks for this wave
                                double radius = waveRadius;

                                // Create particles to show the wave boundary
                                if (level instanceof ServerLevel serverLevel) {
                                    int particles = 20;
                                    for (int i = 0; i < particles; i++) {
                                        double angle = (i * Math.PI * 2) / particles;
                                        double px = playerX + 0.5 + Math.cos(angle) * radius;
                                        double pz = playerZ + 0.5 + Math.sin(angle) * radius;
                                        double py = playerY - 1;

                                        serverLevel.sendParticles(
                                                ParticleTypes.CLOUD,
                                                px, py + 0.5, pz,
                                                2, 0.1, 0.1, 0.1, 0.02
                                        );
                                    }
                                }

                                // Affect blocks in a ring for this wave
                                int affectedBlocks = 0;
                                for (int x = -waveRadius; x <= waveRadius; x++) {
                                    for (int z = -waveRadius; z <= waveRadius; z++) {
                                        // Check if this position is on the current wave ring
                                        double distance = Math.sqrt(x * x + z * z);

                                        // Tolerance for wave thickness
                                        boolean isOnWaveRing = Math.abs(distance - radius) <= 0.7;

                                        if (!isOnWaveRing) {
                                            continue;
                                        }

                                        // Try multiple vertical positions
                                        for (int yOffset = -1; yOffset <= 2; yOffset++) {
                                            BlockPos targetPos = new BlockPos(
                                                    playerX + x,
                                                    playerY - 1 + yOffset, // Check ground level and above
                                                    playerZ + z
                                            );

                                            // Check if this is a valid block to affect
                                            if (isValidBlockForEffect(level, playerPos, targetPos, maxDistance)) {
                                                // Schedule the block lift with a small offset based on distance
                                                int blockDelay = (int)(distance * 2); // Blocks further away lift slightly later

                                                serverPlayer.getServer().tell(
                                                        new TickTask(
                                                                serverPlayer.getServer().getTickCount() + blockDelay,
                                                                () -> createFallingBlock(level, targetPos, waveNumber)
                                                        )
                                                );

                                                affectedBlocks++;
                                                break; // Only affect one block per column
                                            }
                                        }
                                    }
                                }

                                // Entity push for this wave
                                AABB waveAABB = new AABB(
                                        playerX - waveRadius - 0.5, playerY - 2, playerZ - waveRadius - 0.5,
                                        playerX + waveRadius + 0.5, playerY + 3, playerZ + waveRadius + 0.5
                                );

                                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, waveAABB);
                                entities.remove(serverPlayer);

                                for (LivingEntity entity : entities) {
                                    double dx = entity.getX() - (playerX + 0.5);
                                    double dz = entity.getZ() - (playerZ + 0.5);
                                    double distance = Math.sqrt(dx * dx + dz * dz);

                                    // Check if entity is near the wave ring
                                    if (Math.abs(distance - radius) <= 1.0) {
                                        // Push away from center
                                        if (distance > 0.1) {
                                            double strength = 1.5 - (waveNumber * 0.2);
                                            double pushX = (dx / distance) * 0.3 * strength;
                                            double pushZ = (dz / distance) * 0.3 * strength;
                                            double pushY = 0.8 + (waveNumber * 0.1);

                                            entity.push(pushX, pushY, pushZ);

                                            // Damage on first and last waves
                                            if (waveNumber == 1 || waveNumber == waveCount) {
                                                entity.hurt(level.damageSources().magic(), 2.0f);
                                            }
                                        }
                                    }
                                }

                                // Debug output
                                serverPlayer.sendSystemMessage(
                                        Component.literal("Wave " + waveNumber + " activated with " + affectedBlocks + " blocks")
                                );
                            }
                    )
            );
        }
    }

    private boolean isValidBlockForEffect(Level level, BlockPos playerPos, BlockPos targetPos, double maxDistance) {
        // Check distance
        double distance = Math.sqrt(
                (targetPos.getX() - playerPos.getX()) * (targetPos.getX() - playerPos.getX()) +
                        (targetPos.getZ() - playerPos.getZ()) * (targetPos.getZ() - playerPos.getZ())
        );

        if (distance > maxDistance) {
            return false;
        }

        // Check if block is solid and breakable
        BlockState state = level.getBlockState(targetPos);

        // Skip if air or liquid
        if (state.isAir() || state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.LAVA) {
            return false;
        }

        // Check hardness
        if (state.getDestroySpeed(level, targetPos) < 0) {
            return false;
        }

        // Check if above is air (so block can rise)
        if (!level.getBlockState(targetPos.above()).isAir()) {
            return false;
        }

        return true;
    }

    private void createFallingBlock(Level level, BlockPos pos, int waveNumber) {
        if (level.isClientSide) return;

        BlockState state = level.getBlockState(pos);

        // Skip if block is already air or invalid
        if (state.isAir() || !isValidBlockForEffect(level, pos, pos, 10)) {
            return;
        }

        // Remove the original block
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        // Wait 1 tick before spawning falling block to ensure block is removed
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(
                    new TickTask(
                            serverLevel.getServer().getTickCount() + 1,
                            () -> {
                                // Re-check that position is still air
                                if (!level.getBlockState(pos).isAir()) {
                                    return;
                                }

                                // Create the falling block entity
                                FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
                                        serverLevel,
                                        pos,
                                        state
                                );

                                // Configure the falling block
                                fallingBlock.setPos(
                                        pos.getX() + 0.5,
                                        pos.getY(),
                                        pos.getZ() + 0.5
                                );

                                // Calculate upward velocity based on wave
                                float upwardVelocity = 0.6f + (waveNumber * 0.05f);
                                fallingBlock.setDeltaMovement(0, upwardVelocity, 0);

                                // Delay gravity - the falling block will float up before falling
                                fallingBlock.time = 1; // Start falling immediately but with upward velocity

                                // Prevent dropping items
                                fallingBlock.dropItem = false;

                                // Make it break on landing
                                fallingBlock.setHurtsEntities(0.0F, 0);
                                fallingBlock.blocksBuilding = true;

                                // Add some slight horizontal motion for variation
                                if (waveNumber % 2 == 0) {
                                    double angle = Math.random() * Math.PI * 2;
                                    double hSpeed = 0.1;
                                    fallingBlock.setDeltaMovement(
                                            Math.cos(angle) * hSpeed,
                                            upwardVelocity,
                                            Math.sin(angle) * hSpeed
                                    );
                                }

                                // Spawn the entity
                                serverLevel.addFreshEntity(fallingBlock);

                                // Add particle effect
                                serverLevel.sendParticles(
                                        ParticleTypes.CLOUD,
                                        pos.getX() + 0.5,
                                        pos.getY() + 0.5,
                                        pos.getZ() + 0.5,
                                        5,
                                        0.2, 0.1, 0.2,
                                        0.02
                                );

                                // Play sound
                                float pitch = 0.9f + (float)(Math.random() * 0.2);
                                level.playSound(null,
                                        pos,
                                        SoundEvents.STONE_BREAK,
                                        SoundSource.BLOCKS,
                                        0.5f,
                                        pitch
                                );
                            }
                    )
            );
        }
    }


    private void createFallingBlock(Level level, BlockPos pos) {

        // Get blockstate first
        BlockState state = level.getBlockState(pos);

        // Remove the block
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        // Correct 1.21.1 factory method: ONLY takes a BlockPos
        FallingBlockEntity falling = FallingBlockEntity.fall(
                level,
                pos,
                state
        );

        // Move the falling block to the center manually
        falling.setPos(
                pos.getX() + 0.5,
                pos.getY() + 0.2,   // lift slightly for the effect
                pos.getZ() + 0.5
        );

        // Apply upward motion
        falling.setDeltaMovement(0, 0.55, 0);

        // Delay gravity (rising wave effect)
        falling.time = -10;

        // No item drops
        falling.dropItem = false;

        // Spawn entity
        level.addFreshEntity(falling);

        level.playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.7F, 1.0F);
    }
}
   // @Override
   // public Item getMainHandWeapon() {
   //     return ModItems.KABUTOWARI_MAINHAND.get();
   // }
//
   // @Override
   // public Item getOffHandWeapon() {
   //     return ModItems.KABUTOWARI_OFFHAND.get();
   // }
//
   // @Override
   // public Item getUnitedWeapon() {
   //     return ModItems.KABUTOWARI.get();
   // }
//
   // @Override
   // public boolean isMainHand() {
   //     return false;
   // }
//
   // @Override
   // public boolean isUnited() {
   //     return isUnited;
   // }
