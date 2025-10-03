package net.narutoxboruto.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.narutoxboruto.entities.throwables.Kunai;
import net.narutoxboruto.entities.throwables.Shuriken;
import net.narutoxboruto.main.Main;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> MOD_ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Main.MOD_ID);

    // Register your kunai entity:cite[1]
    public static final DeferredHolder<EntityType<?>, EntityType<Kunai>> KUNAI =
            registerEntity("kunai", Kunai::new, MobCategory.MISC, 0.5F, 0.5F);

    public static final DeferredHolder<EntityType<?>, EntityType<Shuriken>> SHURIKEN =
            registerEntity("shuriken", Shuriken::new, MobCategory.MISC, 0.5F, 0.5F);
    // public static final RegistrySupplier<EntityType<ExplosiveKunai>> EXPLOSIVE_KUNAI = registerEntity("explosive_kunai", ExplosiveKunai::new, MobCategory.MISC,
            //         0.5F, 0.5F);

    // public static final RegistrySupplier<EntityType<PoisonSenbon>> POISON_SENBON = registerEntity("poison_senbon", PoisonSenbon::new, MobCategory.MISC,
            //         0.5F, 0.5F);

    // public static final RegistrySupplier<EntityType<Senbon>> SENBON = registerEntity("senbon", Senbon::new, MobCategory.MISC,
            //         0.5F, 0.5F);

    // public static final RegistrySupplier<EntityType<Shuriken>> SHURIKEN = registerEntity("shuriken", Shuriken::new, MobCategory.MISC,
            //         0.5F, 0.5F);

    // public static final RegistrySupplier<EntityType<ThrownFumaShuriken>> FUMA_SHURIKEN = registerEntity("fuma_shuriken", ThrownFumaShuriken::new, MobCategory.MISC,
            //         0.5F, 0.5F);



    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntity(
            String name,
            EntityType.EntityFactory<T> factory,
            MobCategory category,
            float width,
            float height) {

        return MOD_ENTITIES.register(name, () ->
                EntityType.Builder.of(factory, category)
                        .sized(width, height)
                        .build(name) // In 1.21.1, this should be a ResourceLocation
        );
    }


    public static void register(IEventBus eventBus) {
            MOD_ENTITIES.register(eventBus);
        }
    }
