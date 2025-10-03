package net.narutoxboruto.entities.throwables;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.narutoxboruto.entities.ModEntities;
import net.narutoxboruto.items.ModItems;

public class Shuriken extends AbstractThrowableWeapon {

    public Shuriken(EntityType<? extends AbstractThrowableWeapon> entityType, Level level) {
        super(entityType, level);
    }

    public Shuriken(Level world, LivingEntity shooter) {
        // Safer: pass entity type directly or use a different approach
        this(ModEntities.SHURIKEN.get(), world);
        this.setOwner(shooter);
        // Add any additional initialization logic here
    }


    protected ItemStack getDefaultItem() {return new ItemStack(ModItems.SHURIKEN.get());}

    @Override
    public double getBaseDamage() {
        return 3;
    }

    @Override
    public ItemStack getItem() {
        return ModItems.SHURIKEN.get().getDefaultInstance();
    }

    @Override
    protected boolean shouldSpin() {
        return true;
    }
}