package net.narutoxboruto.networking.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.attachments.modes.ChakraControl;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public class ToggleChakraControl implements CustomPacketPayload {

    public static final Type<ToggleChakraControl> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "toggle_chakra_control"));

    public static final StreamCodec<FriendlyByteBuf, ToggleChakraControl> STREAM_CODEC = StreamCodec.ofMember(ToggleChakraControl::toBytes, ToggleChakraControl::new);

    private boolean value;

    public ToggleChakraControl() {
        // Empty constructor for creation
    }

    // Proper deserialization constructor
    public ToggleChakraControl(FriendlyByteBuf buf) {
        // Add any data reading here if your packet needs to send data
    }

    // Proper serialization method
    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle( IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = (ServerPlayer) context.player();
            Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA);
            if (chakra.getValue() > 0) {
                ChakraControl chakraControl = serverPlayer.getData(MainAttachment.CHAKRA_CONTROL);
                chakraControl.setValue(!chakraControl.isActive(), serverPlayer);
            }
            else {
                serverPlayer.displayClientMessage(Component.translatable("msg.no_chakra"), true);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
