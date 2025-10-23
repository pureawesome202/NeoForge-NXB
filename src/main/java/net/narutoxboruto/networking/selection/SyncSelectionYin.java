package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionYin implements CustomPacketPayload {
    private final String yin;

    public static final CustomPacketPayload.Type<SyncSelectionYin> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_yin_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionYin> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionYin::toBytes, SyncSelectionYin::new);

    public SyncSelectionYin(String yin) { this.yin = yin; }

    public SyncSelectionYin(FriendlyByteBuf buf) {
        this.yin = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(yin);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setYinJutsu(yin));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
