package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncReleaseList implements CustomPacketPayload {

    private final String release;

    public static final CustomPacketPayload.Type<SyncReleaseList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_release_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncReleaseList> STREAM_CODEC = StreamCodec.of((buf, string) -> buf.writeInt(Integer.parseInt(string.release)), buf -> new SyncReleaseList(String.valueOf(buf.readInt())));

    public SyncReleaseList(String rank) {
        this.release = rank;
    }

    public SyncReleaseList(FriendlyByteBuf buf) {
        this.release = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.release);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setReleaseList(release));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {return TYPE;}
}
