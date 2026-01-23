package net.narutoxboruto.mixin;

import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.util.RotationUtil;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Camera mixin for wall running view rotation - mirrors Gravity API pattern.
 * 
 * Applies quaternion rotation to the camera based on wall orientation.
 * This makes the world appear rotated when wall running, giving the correct perspective.
 */
@Mixin(Camera.class)
public abstract class MixinCamera {
    
    @Shadow
    private float xRot;
    
    @Shadow
    private float yRot;
    
    @Shadow
    private Quaternionf rotation;
    
    /**
     * Apply wall running rotation to camera after normal setup.
     * 
     * Mirrors Gravity API: inject_setRotation
     * The camera needs to be rotated so the wall appears as the "ground" to the player.
     */
    @Inject(method = "setup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", shift = At.Shift.AFTER))
    private void applyWallRunningRotation(net.minecraft.world.level.BlockGetter level, Entity renderViewEntity, boolean thirdPerson, boolean inverseView, float partialTick, CallbackInfo ci) {
        if (!(renderViewEntity instanceof Player)) return;
        
        RotationUtil.Surface surface = PlayerData.getWallRunningSurface();
        if (surface == RotationUtil.Surface.GROUND) return;
        
        // Get the quaternion rotation for this surface
        Quaternionf surfaceRotation = RotationUtil.getCameraRotationQuaternion(surface);
        this.rotation.mul(surfaceRotation);
    }
    
    /**
     * Set camera rotation angles.
     * 
     * Shadow method to allow modification of rotation.
     */
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);
}
