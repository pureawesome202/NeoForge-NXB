package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncYingList implements CustomPacketPayload {
    private final String yingList;

    public static final CustomPacketPayload.Type<SyncYingList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_ying_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncYingList> STREAM_CODEC = StreamCodec.ofMember(SyncYingList::toBytes, SyncYingList::new);

    public SyncYingList(String yingList) { this.yingList = yingList; }

    public SyncYingList(FriendlyByteBuf buf) {
        this.yingList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(yingList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setEarthList(yingList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
