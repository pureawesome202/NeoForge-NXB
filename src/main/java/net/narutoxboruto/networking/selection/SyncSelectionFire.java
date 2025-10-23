package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionFire implements CustomPacketPayload {

    private final String fireList;

    public static final CustomPacketPayload.Type<SyncSelectionFire> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_fire_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionFire> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionFire::toBytes, SyncSelectionFire::new);

    public SyncSelectionFire(String earthList) { this.fireList = earthList; }

    public SyncSelectionFire(FriendlyByteBuf buf) {
        this.fireList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(fireList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setFireJutsu(fireList));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
