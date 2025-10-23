package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionEarth implements CustomPacketPayload {

    private final String earth;

    public static final CustomPacketPayload.Type<SyncSelectionEarth> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_selection_earth"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionEarth> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionEarth::toBytes, SyncSelectionEarth::new);

    public SyncSelectionEarth(String earth) { this.earth = earth; }

    public SyncSelectionEarth(FriendlyByteBuf buf) {
        this.earth = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(earth);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setEarthJutsu(earth));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
