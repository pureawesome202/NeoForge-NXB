package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncYangList implements CustomPacketPayload {
    private final String yangList;

    public static final CustomPacketPayload.Type<SyncYangList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_yang_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncYangList> STREAM_CODEC = StreamCodec.ofMember(SyncYangList::toBytes, SyncYangList::new);

    public SyncYangList(String yangList) { this.yangList = yangList; }

    public SyncYangList(FriendlyByteBuf buf) {
        this.yangList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(yangList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setYangList(yangList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
