package net.narutoxboruto.main;

import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.client.overlay.ModHudOverlays;
import net.narutoxboruto.entities.ModEntities;
import net.narutoxboruto.items.ModItems;
import net.narutoxboruto.items.ModTab;
import net.narutoxboruto.networking.ModPacketHandler;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import static net.narutoxboruto.attachments.MainAttachment.ATTACHMENT_TYPES;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Main.MOD_ID)
public class Main {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "narutoxboruto";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModTab.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.register(ModPacketHandler.class);
        NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderGuiEvent.Post.class, this::onRenderGui);
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    private void onRenderGui(RenderGuiEvent.Post event) {
        // Copy the render code here or call static method
        ModHudOverlays.onRenderGui(event);
    }
    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
