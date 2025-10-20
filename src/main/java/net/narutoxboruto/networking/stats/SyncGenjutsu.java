package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncGenjutsu implements CustomPacketPayload {
    private final int genjutsu;

    public static final Type<SyncGenjutsu> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_genjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncGenjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncGenjutsu::toBytes, SyncGenjutsu::new);

    public SyncGenjutsu(int genjutsu) {
        this.genjutsu = genjutsu;
    }

    public SyncGenjutsu(FriendlyByteBuf buf) {
        this.genjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(genjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setGenjutsu(genjutsu));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
