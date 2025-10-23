package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionWind implements CustomPacketPayload {
    private final String wind;

    public static final CustomPacketPayload.Type<SyncSelectionWind> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_wind_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionWind> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionWind::toBytes, SyncSelectionWind::new);

    public SyncSelectionWind(String wind) { this.wind = wind; }

    public SyncSelectionWind(FriendlyByteBuf buf) {
        this.wind = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(wind);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setWindJutsu(wind));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
