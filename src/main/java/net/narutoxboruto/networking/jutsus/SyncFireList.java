package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncFireList implements CustomPacketPayload {
    private final String fireList;

    public static final CustomPacketPayload.Type<SyncFireList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_fire_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncFireList> STREAM_CODEC = StreamCodec.ofMember(SyncFireList::toBytes, SyncFireList::new);

    public SyncFireList(String earthList) { this.fireList = earthList; }

    public SyncFireList(FriendlyByteBuf buf) {
        this.fireList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(fireList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setFireList(fireList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
