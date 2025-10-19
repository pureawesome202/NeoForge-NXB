package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncRank implements CustomPacketPayload {

    private final String rank;

    public static final CustomPacketPayload.Type<SyncRank> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_rank"));

    public static final StreamCodec<FriendlyByteBuf, SyncRank> STREAM_CODEC = StreamCodec.of((buf, string) -> buf.writeInt(Integer.parseInt(string.rank)), buf -> new SyncRank(String.valueOf(buf.readInt())));

    public SyncRank(String rank) {
        this.rank = rank;
    }

    public SyncRank(FriendlyByteBuf buf) {
        this.rank = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.rank);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setRank(rank));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {return TYPE;}
}
