package net.narutoxboruto.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.util.RotationUtil;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Entity render dispatcher mixin for wall running model rotation - mirrors Gravity API pattern.
 * 
 * Rotates the player model to match their wall orientation.
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher {
    
    /**
     * Apply wall running rotation to entity model rendering.
     * 
     * Mirrors Gravity API: inject_render
     * Rotates the entity model so it appears correctly oriented on the wall,
     * with feet aligned to the surface.
     */
    @Inject(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.BEFORE)
    )
    private <E extends Entity> void applyWallRunningModelRotation(E entity, double x, double y, double z, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (!(entity instanceof Player)) return;
        
        RotationUtil.Surface surface = PlayerData.getWallRunningSurface();
        if (surface == RotationUtil.Surface.GROUND) return;
        
        // Always push pose to maintain stack balance
        poseStack.pushPose();
        
        // Get the world rotation quaternion for this surface
        // This rotates the model from player space (standing upright) to world space (on surface)
        Quaternionf rotation = RotationUtil.getWorldRotationQuaternion(surface);
        
        // Translate to center of entity for rotation
        double centerHeight = entity.getBbHeight() / 2.0;
        poseStack.translate(0, centerHeight, 0);
        
        // Apply the surface rotation
        poseStack.mulPose(rotation);
        
        // Translate back
        poseStack.translate(0, -centerHeight, 0);
    }
    
    @Inject(
        method = "render",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER)
    )
    private <E extends Entity> void popWallRunningRotation(E entity, double x, double y, double z, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, CallbackInfo ci) {
        if (!(entity instanceof Player)) return;
        
        RotationUtil.Surface surface = PlayerData.getWallRunningSurface();
        if (surface == RotationUtil.Surface.GROUND) return;
        
        // Always pop to match the push
        poseStack.popPose();
    }
}
