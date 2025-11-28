package net.narutoxboruto.items.swords;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.narutoxboruto.items.jutsus.EarthWave.*;
import static net.narutoxboruto.util.BlockUtils.isBlockEntity;

public class Kabutowari extends AbstractAbilitySword {

    public Kabutowari(Properties pProperties) {
        super(SwordCustomTiers.NUIBARI, pProperties);
    }

    @Override
    protected void doSpecialAbility(LivingEntity pTarget, ServerPlayer serverPlayer) {
        int playerX = serverPlayer.getBlockX();
        int playerY = serverPlayer.getBlockY() - 1;
        int playerZ = serverPlayer.getBlockZ();
        BlockPos playerPos = serverPlayer.getOnPos();
        double radius = 4;

        /*Defines the area to search for players, set a list with all Living Entities and removes the caster from the list.*/
        AABB aabb = new AABB(playerX - radius, playerY - radius, playerZ - radius, playerX + radius, playerY + radius,
                playerZ + radius);
        List<LivingEntity> ent = serverPlayer.level().getEntitiesOfClass(LivingEntity.class, aabb);
        ent.remove(serverPlayer);
        if (!serverPlayer.level().isClientSide) {
            if (!playerIsOnAir(serverPlayer.level(), serverPlayer)) {
                for (int distance = 1; distance <= radius; distance++) {
                    for (int x = -distance; x <= distance; x++) {
                        for (int z = -distance; z <= distance; z++) {
                            if (Math.abs(x) == distance || Math.abs(z) == distance) {
                                BlockPos targetPos = new BlockPos(playerX + x, playerY, playerZ + z);
                                if (!isBlockUnbreakable(serverPlayer.level(), targetPos) && isBlockNormalCube(
                                        serverPlayer.level(), targetPos) && !isBlockEntity(serverPlayer.level(), targetPos)
                                        && !isBlockNormalCube(serverPlayer.level(), targetPos.above()) && isWithinCircle(
                                        playerPos, targetPos, radius)) {
                                    //BLOCK INSTANCE AND MOVEMENT
                                    //Used for debugging
                                    //											pLevel.setBlock(targetPos, Blocks.BLACK_WOOL.defaultBlockState(), 2);
                                    //											pLevel.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                                    FallingBlockEntity bLOOOOCK = FallingBlockEntity.fall(serverPlayer.level(),
                                            targetPos, serverPlayer.level().getBlockState(targetPos));
                                    bLOOOOCK.setDeltaMovement(0, 0.3D, 0);
                                    serverPlayer.level().setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                                    serverPlayer.level().addFreshEntity(bLOOOOCK);

                                    /*BLOCK INSTANCE AND MOVEMENT END*/
                                    //
                                }
                            }
                            //									try {
                            //									Thread.sleep(50);
                            //										} catch (InterruptedException e) {
                            //											throw new RuntimeException(e);
                            //							}
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (LivingEntity hurtEntity : ent) {
                        if (hurtEntity.distanceToSqr(playerX, playerY, playerZ) <= (double) distance) {
                            hurtEntity.push(0, 1F, 0);
                            hurtEntity.hurt(hurtEntity.damageSources().magic(), (float) (4 /*+ charge/1.2*/));
                            ent.remove(hurtEntity);
                        }
                    }
                }
            }
        }
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
