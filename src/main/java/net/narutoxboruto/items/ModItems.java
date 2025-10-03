package net.narutoxboruto.items;


import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.narutoxboruto.main.Main.MOD_ID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final DeferredItem<Item> SHURIKEN = ITEMS.registerSimpleItem("shuriken", new Item.Properties());

    public static final DeferredItem<Item> KUNAI = ITEMS.registerSimpleItem("kunai", new Item.Properties());


    public static void register(IEventBus eventBus) {
       ITEMS.register(eventBus); }
}
