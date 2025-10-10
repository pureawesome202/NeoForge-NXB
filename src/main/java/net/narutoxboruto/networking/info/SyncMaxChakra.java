package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncMaxChakra implements CustomPacketPayload {

    private final int maxChakra;

    public static final CustomPacketPayload.Type<SyncMaxChakra> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_maxchakra"));

    public static final StreamCodec<FriendlyByteBuf, SyncMaxChakra> STREAM_CODEC = StreamCodec.of((buf, value) -> buf.writeInt(value.maxChakra), buf -> new SyncMaxChakra(buf.readInt()));


    public SyncMaxChakra(int maxChakra) {
        this.maxChakra = maxChakra;
    }

    public SyncMaxChakra(FriendlyByteBuf buf) {
        this.maxChakra = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(maxChakra);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            PlayerData.setMaxChakra(maxChakra);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
