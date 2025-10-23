package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionYang implements CustomPacketPayload {
    private final String yang;

    public static final CustomPacketPayload.Type<SyncSelectionYang> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_yang_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionYang> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionYang::toBytes, SyncSelectionYang::new);

    public SyncSelectionYang(String yang) { this.yang = yang; }

    public SyncSelectionYang(FriendlyByteBuf buf) {
        this.yang = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(yang);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setYangJutsu(yang));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
