package net.narutoxboruto.items.jutsus;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static net.narutoxboruto.util.BlockUtils.isBlockEntity;

public class EarthWave extends Item {
    public EarthWave(Item.Properties pProperties) {
        super(pProperties);
    }

    public static boolean isWithinCircle(BlockPos center, BlockPos blockPos, double radius) {
        int distanceX = Math.abs(center.getX() - blockPos.getX());
        int distanceZ = Math.abs(center.getZ() - blockPos.getZ());
        return (distanceX * distanceX) + (distanceZ * distanceZ) <= radius * radius;
    }

    public static boolean isBlockUnbreakable(Level pLevel, BlockPos pos) {
        return pLevel.getBlockState(pos).isAir() || pLevel.getBlockState(pos).getDestroySpeed(pLevel, pos) == -1.0F;
    }

    public static boolean playerIsOnAir(Level pLevel, ServerPlayer serverPlayer) {
        return pLevel.getBlockState(serverPlayer.getOnPos()).getBlock() == Blocks.AIR;
    }

    public static boolean isBlockNormalCube(Level pLevel, BlockPos pPos) {
        BlockState state = pLevel.getBlockState(pPos);
        return state.isSolid() && state.isCollisionShapeFullBlock(pLevel, pPos);
    }




   // public void castJutsu(ServerPlayer serverPlayer, Level pLevel, double charge) {
   //     int playerX = serverPlayer.getBlockX();
   //     int playerY = serverPlayer.getBlockY() - 1;
   //     int playerZ = serverPlayer.getBlockZ();
   //     BlockPos playerPos = serverPlayer.getOnPos();
   //     double radius = 4;
   //     if (charge >= 2) {
   //         radius = radius + charge * 0.6;
   //     }
   //     /*Defines the area to search for players, set a list with all Living Entities and removes the caster from the list.*/
   //     AABB aabb = new AABB(playerX - radius, playerY - radius, playerZ - radius, playerX + radius,
   //             playerY + radius, playerZ + radius);
   //     List<LivingEntity> ent = serverPlayer.level().getEntitiesOfClass(LivingEntity.class, aabb);
   //     ent.remove(serverPlayer);
   //     if (!pLevel.isClientSide) {
   //         if (!playerIsOnAir(pLevel, serverPlayer)) {
   //             for (int distance = 1; distance <= radius; distance++) {
   //                 for (int x = -distance; x <= distance; x++) {
   //                     for (int z = -distance; z <= distance; z++) {
   //                         if (Math.abs(x) == distance || Math.abs(z) == distance) {
   //                             BlockPos targetPos = new BlockPos(playerX + x, playerY, playerZ + z);
   //                             if (!isBlockUnbreakable(pLevel, targetPos) && isBlockNormalCube(pLevel,
   //                                     targetPos) && !isBlockEntity(pLevel, targetPos) && !isBlockNormalCube(
   //                                     pLevel, targetPos.above()) && isWithinCircle(playerPos, targetPos,
   //                                     radius)) {
   //                                 //BLOCK INSTANCE AND MOVEMENT
   //                                 //Used for debugging
   //                                 //											pLevel.setBlock(targetPos, Blocks.BLACK_WOOL.defaultBlockState(), 2);
   //                                 //											pLevel.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
   //                                 FallingBlockEntity bLOOOOCK = new FallingBlockEntity(pLevel,
   //                                         targetPos.getX() + 0.5, targetPos.getY() + 0.5,
   //                                         targetPos.getZ() + 0.5, pLevel.getBlockState(targetPos));
   //                                 pLevel.setBlock(targetPos, pLevel.getBlockState(targetPos).getFluidState()
   //                                         .createLegacyBlock(), 3);
   //                                 bLOOOOCK.push(0, 0.46D, 0);
   //                                  pLevel.addFreshEntity(bLOOOOCK);
////
   //                                 /*BLOCK INSTANCE AND MOVEMENT END*/
   ////                                 //
   //                             }
   //                         }
   //                     }
   //                 }
   //                 try {
   //                     Thread.sleep(250);
   //                 } catch (InterruptedException e) {
   //                     throw new RuntimeException(e);
   //                 }
   //                 for (LivingEntity hurtEntity : ent) {
   //                     if (hurtEntity.distanceToSqr(playerX, playerY, playerZ) <= (double) distance * 2) {
   //                         hurtEntity.push(0, 1F, 0);
   //                         hurtEntity.hurt(DamageSource.MAGIC, (float) (4 /*+ charge/1.2*/));
   //                         ent.remove(hurtEntity);
   //                     }
   //                 }
   //             }
   //             //						for (Player tremePlayer : ent){
   //             //							shakeScreen(tremePlayer, 12);
   //             //						}
   //         }
   //     }
   // }

}

