package net.narutoxboruto.entities.throwables;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.narutoxboruto.entities.ModEntities;
import net.narutoxboruto.items.ModItems;

public class Kunai extends AbstractThrowableWeapon {

    public Kunai(EntityType<? extends AbstractThrowableWeapon> entityType, Level level) {
        super(entityType, level);
    }

    public Kunai(Level world, LivingEntity shooter) {
        // Safer: pass entity type directly or use a different approach
        this(ModEntities.KUNAI.get(), world);
        this.setOwner(shooter);
        // Add any additional initialization logic here
    }

    protected ItemStack getDefaultItem() {return new ItemStack(ModItems.KUNAI.get());}

    @Override
    public double getBaseDamage() {
        return 3;
    }

    @Override
    public ItemStack getItem() {
        return ModItems.KUNAI.get().getDefaultInstance();
    }
}
