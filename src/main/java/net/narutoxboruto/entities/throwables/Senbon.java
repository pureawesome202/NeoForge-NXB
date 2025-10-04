package net.narutoxboruto.entities.throwables;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.narutoxboruto.entities.ModEntities;
import net.narutoxboruto.items.ModItems;

public class Senbon extends AbstractThrowableWeapon {
    public Senbon(EntityType<? extends AbstractThrowableWeapon> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public Senbon(Level world, LivingEntity shooter) {
        // Safer: pass entity type directly or use a different approach
        this(ModEntities.SENBON.get(), world);
        this.setOwner(shooter);
        // Add any additional initialization logic here
    }

    @Override
    public double getBaseDamage() {
        return 1.5;
    }

    @Override
    public ItemStack getItem() {
        return ModItems.SENBON.get().getDefaultInstance();
    }
}
