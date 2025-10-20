package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncWindList implements CustomPacketPayload {
    private final String windList;

    public static final CustomPacketPayload.Type<SyncWindList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_wind_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncWindList> STREAM_CODEC = StreamCodec.ofMember(SyncWindList::toBytes, SyncWindList::new);

    public SyncWindList(String windList) { this.windList = windList; }

    public SyncWindList(FriendlyByteBuf buf) {
        this.windList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(windList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setWindList(windList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
