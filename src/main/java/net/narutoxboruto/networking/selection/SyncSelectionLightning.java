package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionLightning implements CustomPacketPayload {

    private final String lightning;

    public static final CustomPacketPayload.Type<SyncSelectionLightning> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_lightning_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionLightning> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionLightning::toBytes, SyncSelectionLightning::new);

    public SyncSelectionLightning(String lightning) { this.lightning = lightning; }

    public SyncSelectionLightning(FriendlyByteBuf buf) {
        this.lightning = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(lightning);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setLightningJutsu(lightning));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
