package net.narutoxboruto.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.modes.WallRunning;
import net.narutoxboruto.util.RotationUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Core entity mixin for coordinate transformation - mirrors Gravity API pattern.
 * 
 * Key transformations:
 * - Bounding box: Transforms from player-relative to world space
 * - Eye position: Adjusts for rotated orientation
 * - Rotation vectors: Transforms look direction from player to world space
 * 
 * This is the foundational layer that makes wall running work correctly.
 */
@Mixin(Entity.class)
public abstract class MixinEntity {
    
    @Shadow
    public abstract Vec3 position();
    
    @Shadow
    public abstract double getX();
    
    @Shadow
    public abstract double getY();
    
    @Shadow
    public abstract double getZ();
    
    @Shadow
    public abstract float getEyeHeight();
    
    @Shadow
    public abstract Vec3 getDeltaMovement();
    
    @Shadow
    public abstract void setDeltaMovement(Vec3 motion);
    
    /**
     * Transform the bounding box from player space to world space.
     * 
     * Mirrors Gravity API: inject_calculateBoundingBox
     * When on a wall, the player's bounding box needs to be rotated to match the wall orientation.
     */
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void transformBoundingBox(CallbackInfoReturnable<AABB> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player player)) return;
        
        RotationUtil.Surface surface = player.level().isClientSide
            ? net.narutoxboruto.client.PlayerData.getWallRunningSurface()
            : player.getData(MainAttachment.WALL_RUNNING).getSurface();
        
        if (surface == RotationUtil.Surface.GROUND) return;
        
        AABB originalBox = cir.getReturnValue();
        Vec3 pos = position();
        
        // Get the box in player-relative space (centered at origin)
        AABB playerSpaceBox = originalBox.move(-pos.x, -pos.y, -pos.z);
        
        // Add small margin for ceiling attachment
        if (surface == RotationUtil.Surface.CEILING) {
            playerSpaceBox = playerSpaceBox.inflate(0, -1.0E-6, 0);
        }
        
        // Transform to world space and move back to position
        AABB worldSpaceBox = RotationUtil.boxPlayerToWorld(playerSpaceBox, surface);
        cir.setReturnValue(worldSpaceBox.move(pos));
    }
    
    /**
     * Transform rotation vectors (look direction) from player space to world space.
     * 
     * Mirrors Gravity API: inject_getRotationVector
     * The player's view direction needs to be transformed based on their wall orientation.
     */
    @Inject(method = "getViewVector", at = @At("RETURN"), cancellable = true)
    private void transformRotationVector(float partialTicks, CallbackInfoReturnable<Vec3> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player player)) return;
        
        RotationUtil.Surface surface = player.level().isClientSide
            ? net.narutoxboruto.client.PlayerData.getWallRunningSurface()
            : player.getData(MainAttachment.WALL_RUNNING).getSurface();
        
        if (surface == RotationUtil.Surface.GROUND) return;
        
        Vec3 playerSpaceVec = cir.getReturnValue();
        Vec3 worldSpaceVec = RotationUtil.vecPlayerToWorld(playerSpaceVec, surface);
        cir.setReturnValue(worldSpaceVec);
    }
    
    /**
     * Adjust eye position for wall running orientation.
     * 
     * Mirrors Gravity API: inject_getEyePos
     * Eye position needs to account for the rotated player model.
     */
    @Inject(method = "getEyePosition()Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"), cancellable = true)
    private void transformEyePosition(CallbackInfoReturnable<Vec3> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player player)) return;
        
        RotationUtil.Surface surface = player.level().isClientSide
            ? net.narutoxboruto.client.PlayerData.getWallRunningSurface()
            : player.getData(MainAttachment.WALL_RUNNING).getSurface();
        
        if (surface == RotationUtil.Surface.GROUND) return;
        
        Vec3 pos = position();
        float eyeHeight = getEyeHeight();
        
        // In player space, eye is at (0, eyeHeight, 0)
        // Transform this to world space based on surface orientation
        Vec3 playerSpaceEye = new Vec3(0, eyeHeight, 0);
        Vec3 worldSpaceOffset = RotationUtil.vecPlayerToWorld(playerSpaceEye, surface);
        
        cir.setReturnValue(pos.add(worldSpaceOffset));
    }
    
    /**
     * Override onGround to return true when wall running.
     * This is CRITICAL - without this, the player can't move while on walls!
     * Mirrors Gravity API's inject_onGround mixin.
     */
    @Inject(method = "onGround", at = @At("RETURN"), cancellable = true)
    private void wallRunningOnGround(CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof Player player)) return;
        
        boolean isWallRunning = player.getData(MainAttachment.WALL_RUNNING).isWallRunning();
        if (isWallRunning) {
            if (player.tickCount % 40 == 0) {
                System.out.println("[MixinEntity] onGround() override returning TRUE (wall running)");
            }
            cir.setReturnValue(true);
        }
    }
}
