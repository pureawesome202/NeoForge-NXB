package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionWater implements CustomPacketPayload {
    private final String water;

    public static final CustomPacketPayload.Type<SyncSelectionWater> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_water_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionWater> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionWater::toBytes, SyncSelectionWater::new);

    public SyncSelectionWater(String earthList) { this.water = earthList; }

    public SyncSelectionWater(FriendlyByteBuf buf) {
        this.water = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(water);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setWaterJutsu(water));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
