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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.narutoxboruto.entities.ModEntities;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;

/**
 * Water Dragon Projectile - Powerful Water Release Jutsu
 * 
 * A fast-moving dragon made of compressed water that travels in a straight line.
 * Creates a large water explosion on impact.
 * 
 * TODO: This is a PLACEHOLDER implementation awaiting:
 * - Custom dragon model from owner
 * - Custom textures from owner
 * - Custom animation system
 * 
 * Current behavior:
 * - Fast projectile (1.5x speed of Shark Bomb)
 * - Long range (20 blocks)
 * - High damage (20)
 * - Large water explosion on impact
 * - No homing (travels straight)
 */
public class WaterDragonEntity extends Projectile {
    
    private int age = 0;
    
    // Configuration
    private static final float DAMAGE = 20.0F; // High damage
    private static final float SPEED = 1.2F; // Fast moving
    private static final double MAX_RANGE = 20.0; // Long range
    private static final int MAX_LIFETIME = 120; // 6 seconds max
    private static final float EXPLOSION_RADIUS = 4.0F; // Large explosion
    
    private Vec3 startPos;
    
    public WaterDragonEntity(EntityType<? extends WaterDragonEntity> entityType, Level level) {
        super(entityType, level);
    }
    
    public WaterDragonEntity(Level level, LivingEntity shooter) {
        this(ModEntities.WATER_DRAGON.get(), level);
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
            this.explodeWithWater();
            this.discard();
            return;
        }
        
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
        // TODO: Replace with custom dragon particle effects when model is provided
        if (this.level() instanceof ServerLevel serverLevel) {
            // Create a water trail effect
            serverLevel.sendParticles(ParticleTypes.DRIPPING_WATER,
                    this.getX(), this.getY(), this.getZ(),
                    5, 0.3, 0.3, 0.3, 0.02);
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.getX(), this.getY(), this.getZ(),
                    3, 0.2, 0.2, 0.2, 0.05);
            serverLevel.sendParticles(ParticleTypes.FALLING_WATER,
                    this.getX(), this.getY(), this.getZ(),
                    2, 0.15, 0.15, 0.15, 0.01);
        }
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
            
            // Deal high water damage
            livingTarget.hurt(this.damageSources().mobProjectile(this, 
                    this.getOwner() instanceof LivingEntity le ? le : null), DAMAGE);
            
            // Strong knockback effect
            Vec3 knockback = this.getDeltaMovement().normalize().scale(1.0);
            livingTarget.setDeltaMovement(livingTarget.getDeltaMovement().add(knockback));
            
            // Play impact sound
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.5F, 0.8F);
        }
        
        // Create large water explosion on entity hit
        explodeWithWater();
        this.discard();
    }
    
    @Override
    protected void onHitBlock(BlockHitResult result) {
        // Create large water explosion on block hit
        explodeWithWater();
        
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.5F, 0.8F);
        
        this.discard();
    }
    
    /**
     * Create a large water explosion effect on impact.
     * TODO: Replace with custom explosion model when provided by owner.
     */
    private void explodeWithWater() {
        if (this.level() instanceof ServerLevel serverLevel) {
            // Create a massive water splash effect (placeholder)
            // Center explosion
            serverLevel.sendParticles(ParticleTypes.SPLASH,
                    this.getX(), this.getY(), this.getZ(),
                    50, 1.0, 1.0, 1.0, 0.5);
            
            // Falling water from above
            serverLevel.sendParticles(ParticleTypes.FALLING_WATER,
                    this.getX(), this.getY() + 2, this.getZ(),
                    40, 1.5, 0.5, 1.5, 0.2);
            
            // Dripping water particles
            serverLevel.sendParticles(ParticleTypes.DRIPPING_WATER,
                    this.getX(), this.getY() + 1, this.getZ(),
                    30, 1.0, 1.0, 1.0, 0.1);
            
            // Rain particles for effect
            serverLevel.sendParticles(ParticleTypes.RAIN,
                    this.getX(), this.getY() + 3, this.getZ(),
                    25, 2.0, 0.5, 2.0, 0.3);
            
            // Damage nearby entities (explosion effect)
            for (LivingEntity nearby : this.level().getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(EXPLOSION_RADIUS))) {
                if (nearby != this.getOwner() && nearby.isAlive()) {
                    double distance = nearby.distanceTo(this);
                    if (distance <= EXPLOSION_RADIUS) {
                        // Damage falls off with distance
                        float explosionDamage = (float) (DAMAGE * 0.5 * (1.0 - (distance / EXPLOSION_RADIUS)));
                        nearby.hurt(this.damageSources().mobProjectile(this,
                                this.getOwner() instanceof LivingEntity le ? le : null), explosionDamage);
                        
                        // Knockback away from explosion center
                        Vec3 knockbackDir = nearby.position().subtract(this.position()).normalize();
                        nearby.setDeltaMovement(nearby.getDeltaMovement().add(knockbackDir.scale(0.5)));
                    }
                }
            }
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
