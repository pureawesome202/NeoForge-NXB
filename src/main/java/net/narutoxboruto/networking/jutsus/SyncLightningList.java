package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncLightningList implements CustomPacketPayload {
    private final String lightningList;

    public static final CustomPacketPayload.Type<SyncLightningList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_lightning_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncLightningList> STREAM_CODEC = StreamCodec.ofMember(SyncLightningList::toBytes, SyncLightningList::new);

    public SyncLightningList(String lightningList) { this.lightningList = lightningList; }

    public SyncLightningList(FriendlyByteBuf buf) {
        this.lightningList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(lightningList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setLightningList(lightningList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
