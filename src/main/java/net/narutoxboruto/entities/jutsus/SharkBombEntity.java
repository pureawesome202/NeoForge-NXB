package net.narutoxboruto.entities.jutsus;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.narutoxboruto.entities.ModEntities;

import java.util.List;

/**
 * Shark Bomb Projectile - Water Release Jutsu
 * 
 * A homing projectile that automatically seeks nearby enemies.
 * Player can influence targeting by aiming their crosshair at specific targets.
 * Maximum range: 10 blocks
 */
public class SharkBombEntity extends Projectile {
    
    private int age = 0;
    private LivingEntity targetEntity = null;
    
    // Configuration
    private static final float DAMAGE = 12.0F;
    private static final float SPEED = 0.8F;
    private static final double MAX_RANGE = 10.0; // Maximum travel distance
    private static final double HOMING_RANGE = 15.0; // Range to detect targets
    private static final double CROSSHAIR_INFLUENCE = 0.3; // How much player aim affects trajectory
    private static final double HOMING_STRENGTH = 0.15; // How aggressively it homes
    private static final int MAX_LIFETIME = 100; // 5 seconds max
    
    private Vec3 startPos;
    
    public SharkBombEntity(EntityType<? extends SharkBombEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public SharkBombEntity(Level level, LivingEntity shooter) {
        this(ModEntities.SHARK_BOMB.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.startPos = this.position();
        
        // Set initial velocity in the direction the player is looking
        Vec3 lookVec = shooter.getLookAngle();
        this.setDeltaMovement(lookVec.scale(SPEED));
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        // No additional synced data needed
    }
    
    @Override
    public void tick() {
        super.tick();
        
        this.age++;
        
        // Check for max lifetime or max range
        if (this.age > MAX_LIFETIME || this.startPos.distanceTo(this.position()) > MAX_RANGE) {
            this.explodeInWater();
            this.discard();
            return;
        }
        
        // Update homing behavior
        updateHoming();
        
        // Perform hit detection
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
            return;
        }
        
        // Update position based on velocity
        Vec3 velocity = this.getDeltaMovement();
        this.setPos(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);
        
        // Spawn water particles while flying (placeholder visual)
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.DRIPPING_WATER,
                    this.getX(), this.getY(), this.getZ(),
                    2, 0.2, 0.2, 0.2, 0.01);
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.getX(), this.getY(), this.getZ(),
                    1, 0.1, 0.1, 0.1, 0.02);
        }
    }
    
    /**
     * Update homing behavior - find target and adjust trajectory.
     */
    private void updateHoming() {
        if (!(this.getOwner() instanceof Player player)) {
            return;
        }
        
        // Find or update target
        if (this.targetEntity == null || !this.targetEntity.isAlive() || 
            this.targetEntity.distanceTo(this) > HOMING_RANGE) {
            this.targetEntity = findBestTarget(player);
        }
        
        if (this.targetEntity != null) {
            // Calculate direction to target
            Vec3 targetPos = this.targetEntity.position().add(0, this.targetEntity.getBbHeight() / 2, 0);
            Vec3 toTarget = targetPos.subtract(this.position()).normalize();
            
            // Get player's look direction for crosshair influence
            Vec3 playerLook = player.getLookAngle();
            
            // Blend target direction with player's aim
            Vec3 desiredDirection = toTarget.scale(1.0 - CROSSHAIR_INFLUENCE)
                    .add(playerLook.scale(CROSSHAIR_INFLUENCE))
                    .normalize();
            
            // Smoothly adjust velocity toward desired direction
            Vec3 currentVel = this.getDeltaMovement().normalize();
            Vec3 newDirection = currentVel.scale(1.0 - HOMING_STRENGTH)
                    .add(desiredDirection.scale(HOMING_STRENGTH))
                    .normalize();
            
            this.setDeltaMovement(newDirection.scale(SPEED));
        }
    }
    
    /**
     * Find the best target to home in on.
     * Prioritizes targets closer to the player's crosshair.
     */
    private LivingEntity findBestTarget(Player player) {
        AABB searchBox = new AABB(
                this.getX() - HOMING_RANGE, this.getY() - HOMING_RANGE, this.getZ() - HOMING_RANGE,
                this.getX() + HOMING_RANGE, this.getY() + HOMING_RANGE, this.getZ() + HOMING_RANGE
        );
        
        List<LivingEntity> nearbyEntities = this.level().getEntitiesOfClass(
                LivingEntity.class, searchBox,
                entity -> entity != player && entity.isAlive() && !entity.isSpectator()
        );
        
        if (nearbyEntities.isEmpty()) {
            return null;
        }
        
        // Find entity closest to player's crosshair
        Vec3 playerLook = player.getLookAngle();
        Vec3 playerPos = player.getEyePosition();
        
        LivingEntity bestTarget = null;
        double bestScore = -1;
        
        for (LivingEntity entity : nearbyEntities) {
            Vec3 toEntity = entity.position().subtract(playerPos).normalize();
            double dotProduct = playerLook.dot(toEntity); // How aligned with crosshair
            double distance = entity.distanceTo(this);
            
            // Score based on crosshair alignment and distance
            double score = dotProduct - (distance / HOMING_RANGE) * 0.3;
            
            if (score > bestScore) {
                bestScore = score;
                bestTarget = entity;
            }
        }
        
        return bestTarget;
    }
    
    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        
        if (!this.level().isClientSide()) {
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                this.onHitEntity((EntityHitResult) hitResult);
            } else if (hitResult.getType() == HitResult.Type.BLOCK) {
                this.onHitBlock((BlockHitResult) hitResult);
            }
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        
        if (target instanceof LivingEntity livingTarget) {
            // Don't damage the caster
            if (livingTarget == this.getOwner()) {
                return;
            }
            
            // Deal water damage
            livingTarget.hurt(this.damageSources().mobProjectile(this, 
                    this.getOwner() instanceof LivingEntity le ? le : null), DAMAGE);
            
            // Knockback effect
            Vec3 knockback = this.getDeltaMovement().normalize().scale(0.5);
            livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().add(knockback));
            
            // Play hit sound
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.PLAYER_SPLASH_HIGH_SPEED, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
        
        // Create water splash effect
        explodeInWater();
        this.discard();
    }
    
    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Create water splash on block hit
        explodeInWater();
        
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.PLAYER_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
        
        this.discard();
    }
    
    /**
     * Create a water splash effect on impact.
     */
    private void explodeInWater() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // Large water splash particles
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.getX(), this.getY(), this.getZ(),
                    20, 0.5, 0.5, 0.5, 0.3);
            serverLevel.sendParticles(ParticleTypes.FALLING_WATER,
                    this.getX(), this.getY() + 1, this.getZ(),
                    15, 0.3, 0.3, 0.3, 0.1);
        }
    }
    
    @Override
    protected boolean canHitEntity(Entity entity) {
        // Don't hit the owner
        return super.canHitEntity(entity) && entity != this.getOwner();
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.age = compound.getInt("Age");
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", this.age);
    }
}
