package net.narutoxboruto.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.capabilities.info.Chakra;
import net.narutoxboruto.capabilities.info.MaxChakra;
import net.narutoxboruto.networking.info.SyncChakra;
import net.narutoxboruto.networking.info.SyncMaxChakra;
import net.narutoxboruto.networking.misc.ToggleSwordAbility;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0"); // Your network version

        // Register the SyncChakra packet to be sent from server to client
        registrar.playToClient(SyncChakra.TYPE, SyncChakra.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncMaxChakra.TYPE, SyncMaxChakra.STREAM_CODEC, (payload, context) -> payload.handle(context));

        // Fixed ToggleSwordAbility registration
        registrar.playToServer(ToggleSwordAbility.TYPE, ToggleSwordAbility.STREAM_CODEC, (payload, context) -> payload.handle(context));

    }
    public static final AttachmentType<Chakra> CHAKRA = AttachmentType.builder(Chakra::new).build();
    public static final AttachmentType<MaxChakra> MAX_CHAKRA = AttachmentType.builder(MaxChakra::new).build();


    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
