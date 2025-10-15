package net.narutoxboruto.networking.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.capabilities.InfoCapabilityProvider;
import net.narutoxboruto.capabilities.info.Chakra;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RechargeChakra implements CustomPacketPayload {

    public static final Type<RechargeChakra> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "recharge_chakra"));

    public static final StreamCodec<FriendlyByteBuf, RechargeChakra> STREAM_CODEC = StreamCodec.ofMember(RechargeChakra::toBytes, RechargeChakra::new);

    public RechargeChakra() {
        // Empty constructor for creation
    }

    // Proper deserialization constructor
    public RechargeChakra(FriendlyByteBuf buf) {
        // Add any data reading here if your packet needs to send data
    }

    // Proper serialization method
    public void toBytes(FriendlyByteBuf buf) {
        // Add any data writing here if your packet needs to send data
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = (ServerPlayer) context.player();

            // Use the data attachment system directly
            Chakra chakra = serverPlayer.getData(InfoCapabilityProvider.CHAKRA);
            chakra.addValue(1, serverPlayer);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
