package net.narutoxboruto.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.narutoxboruto.main.Main.MOD_ID;

public class ModTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    //EXAMPLE ITEM NEEDS TO BE CHANGED

  //  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THORWABLE = CREATIVE_MODE_TAB.register("throwable", () -> CreativeModeTab.builder()
  //          .title(Component.translatable("itemGroup.throwable")) //The language key for the title of your CreativeModeTab
  //          .withTabsBefore(CreativeModeTabs.COMBAT)
  //          .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
  //          .displayItems((parameters, output) -> {
  //              output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
  //          }).build());

    public static void register(IEventBus eventBus) {CREATIVE_MODE_TAB.register(eventBus);}
}
