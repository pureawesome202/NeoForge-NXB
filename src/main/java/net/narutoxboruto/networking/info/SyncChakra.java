package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncChakra implements CustomPacketPayload {
    private final int chakra;

    public static final CustomPacketPayload.Type<SyncChakra> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_chakra"));

    public static final StreamCodec<FriendlyByteBuf, SyncChakra> STREAM_CODEC = StreamCodec.of((buf, value) -> buf.writeInt(value.chakra), buf -> new SyncChakra(buf.readInt()));

    public SyncChakra(int chakra) {
        this.chakra = chakra;
    }

    public SyncChakra(FriendlyByteBuf buf) {
        this.chakra = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(chakra);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            // Make sure PlayerData handles the client-side properly
            PlayerData.setChakra(chakra);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
