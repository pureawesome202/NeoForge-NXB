package net.narutoxboruto.items;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.narutoxboruto.items.misc.ChakraPaper;
import net.narutoxboruto.items.swords.*;
import net.narutoxboruto.items.throwables.FumaShurikenItem;
import net.narutoxboruto.items.throwables.ThrowableWeaponItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


import static net.narutoxboruto.main.Main.MOD_ID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final DeferredItem<Item> SHURIKEN = ITEMS.register("shuriken", () -> new ThrowableWeaponItem(new Item.Properties().stacksTo(64), "shuriken"));

    public static final DeferredItem<Item> KUNAI = ITEMS.register("kunai", () -> new ThrowableWeaponItem(new Item.Properties().stacksTo(64), "kunai"));

    public static final DeferredItem<Item> EXPLOSIVE_KUNAI = ITEMS.register("explosive_kunai", () -> new ThrowableWeaponItem(new Item.Properties().stacksTo(16), "explosive_kunai"));

    public static final DeferredItem<Item> SENBON = ITEMS.register("senbon", () -> new ThrowableWeaponItem(new Item.Properties().stacksTo(64), "senbon"));

    public static final DeferredItem<Item> POISON_SENBON = ITEMS.register("poison_senbon", () -> new ThrowableWeaponItem(new Item.Properties().stacksTo(64), "poison_senbon"));

    public static final DeferredItem<Item> FUMA_SHURIKEN = ITEMS.register("fuma_shuriken", () -> new FumaShurikenItem(new Item.Properties().stacksTo(1),"fuma_shuriken"));

    public static final DeferredItem<Item> SAMEHADA = ITEMS.register("samehada", () -> new Samehada(new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(SwordCustomTiers.SAMEHADA, 2, -2.5f))));

    public static final DeferredItem<Item> KUBIKIRIBOCHO = ITEMS.register("kubikiribocho", () -> new Kubikiribocho(new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(SwordCustomTiers.KUBIKIRIBOCHO, 6, -3f))));

    public static final DeferredItem<Item> SHIBUKI = ITEMS.register("shibuki", () -> new Shibuki(new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(SwordCustomTiers.SHIBUKI, 2, -2f))));

    public static final DeferredItem<Item> NUIBARI = ITEMS.register("nuibari", () -> new Nuibari(new Item.Properties().stacksTo(1).attributes(SwordItem.createAttributes(SwordCustomTiers.NUIBARI, 2, -1.5f))));

    public static final DeferredItem<Item> CHAKRA_PAPER = ITEMS.register("chakra_paper", () -> new ChakraPaper(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
       ITEMS.register(eventBus);
    }
}