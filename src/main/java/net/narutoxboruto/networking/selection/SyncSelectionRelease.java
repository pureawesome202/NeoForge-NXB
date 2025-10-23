package net.narutoxboruto.networking.selection;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSelectionRelease implements CustomPacketPayload {

    private final String release;

    public static final CustomPacketPayload.Type<SyncSelectionRelease> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_release_selection"));

    public static final StreamCodec<FriendlyByteBuf, SyncSelectionRelease> STREAM_CODEC = StreamCodec.ofMember(SyncSelectionRelease::toBytes, SyncSelectionRelease::new);

    public SyncSelectionRelease(String release) { this.release = release; }

    public SyncSelectionRelease(FriendlyByteBuf buf) {
        this.release = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(release);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setSelectedRelease(release));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
