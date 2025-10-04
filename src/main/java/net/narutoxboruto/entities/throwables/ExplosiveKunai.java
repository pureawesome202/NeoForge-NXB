package net.narutoxboruto.entities.throwables;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.narutoxboruto.entities.ModEntities;
import net.narutoxboruto.items.ModItems;

public class ExplosiveKunai extends AbstractThrowableWeapon {
    public ExplosiveKunai(EntityType<? extends AbstractThrowableWeapon> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public ExplosiveKunai(Level world, LivingEntity shooter) {
        // Safer: pass entity type directly or use a different approach
        this(ModEntities.EXPLOSIVE_KUNAI.get(), world);
        this.setOwner(shooter);
        // Add any additional initialization logic here
    }

    @Override
    public double getBaseDamage() {
        return 0;
    }

    @Override
    public ItemStack getItem() {
        return ModItems.EXPLOSIVE_KUNAI.get().getDefaultInstance();
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();

            // Trigger explosion only if the hitResult is not null (ensures collision detection)
            if (hitResult.getType() == HitResult.Type.BLOCK || hitResult.getType() == HitResult.Type.ENTITY) {
                serverLevel.explode(
                        null, x, y, z, 3.0F, false, Level.ExplosionInteraction.BLOCK
                );
                this.discard();  // Remove entity after explosion
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.discard();
    }
}
