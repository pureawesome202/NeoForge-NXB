package net.narutoxboruto.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.modes.WallRunning;
import net.narutoxboruto.util.RotationUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * LivingEntity travel mixin for wall running movement - mirrors Gravity API pattern.
 * 
 * Transforms movement vectors from world space to player-relative space,
 * allowing natural movement controls while wall running.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityTravel {
    
    /**
     * Transform travel movement vector from world space to player space.
     * 
     * Mirrors Gravity API: modify_travel_Vec3d
     * Converts the travel vector to player-relative coordinates so WASD controls work naturally.
     */
    @ModifyVariable(
        method = "travel",
        at = @At(value = "HEAD"),
        argsOnly = true
    )
    private Vec3 transformTravelVector(Vec3 travelVector) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof Player player)) return travelVector;
        
        // Use server attachment on server, client cache on client
        RotationUtil.Surface surface = player.level().isClientSide
            ? net.narutoxboruto.client.PlayerData.getWallRunningSurface()
            : player.getData(MainAttachment.WALL_RUNNING).getSurface();
        
        if (surface == RotationUtil.Surface.GROUND) return travelVector;
        
        // Debug logging to verify this is being called
        if (travelVector.lengthSqr() > 0.001 && player.tickCount % 10 == 0) {
            System.out.println("[MixinTravel] Transforming travel vector!");
            System.out.println("  Input: " + travelVector);
            System.out.println("  Surface: " + surface);
        }
        
        // Transform the travel input from PLAYER space to WORLD space based on surface
        // Vanilla expects travel vector in player-space (strafe, vertical, forward).
        // We rotate it so forward (Z) becomes "up" along the wall.
        Vec3 transformed = RotationUtil.vecPlayerToWorld(travelVector, surface);
        
        if (travelVector.lengthSqr() > 0.001 && player.tickCount % 10 == 0) {
            System.out.println("  Output: " + transformed);
        }
        
        return transformed;
    }
}
