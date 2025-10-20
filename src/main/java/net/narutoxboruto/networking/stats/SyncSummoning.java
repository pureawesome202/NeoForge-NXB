package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSummoning implements CustomPacketPayload {
    private final int summoning;

    public static final CustomPacketPayload.Type<SyncSummoning> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_summoning"));

    public static final StreamCodec<FriendlyByteBuf, SyncSummoning> STREAM_CODEC = StreamCodec.ofMember(SyncSummoning::toBytes, SyncSummoning::new);

    public SyncSummoning(int summoning) {
        this.summoning = summoning;
    }

    public SyncSummoning(FriendlyByteBuf buf) {
        this.summoning = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(summoning);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setSummoning(summoning));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
