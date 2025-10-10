package net.narutoxboruto.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.narutoxboruto.items.swords.AbstractAbilitySword;
import net.narutoxboruto.main.Main;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.misc.ToggleSwordAbility;
import net.narutoxboruto.util.ModKeyBinds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class ClientEvents {

    @EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void specialActionKeybind(InputEvent.Key event) {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player != null) {
                ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (ModKeyBinds.SPECIAL_ACTION.consumeClick()) {
                    if (itemStack.getItem() instanceof AbstractAbilitySword) {
                        ModPacketHandler.sendToServer(new ToggleSwordAbility());
                    }
                }
            }
        }


    }
    @EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            //event.register(ModKeyBinds.OPEN_GUI);
            //event.register(ModKeyBinds.CHAKRA_CONTROL);
            event.register(ModKeyBinds.SPECIAL_ACTION);
        }
    }
}
