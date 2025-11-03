package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncShinobiPoints implements CustomPacketPayload {
    private final int shinobi_points;

    public static final CustomPacketPayload.Type<SyncShinobiPoints> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_shinobipoints"));

    public static final StreamCodec<FriendlyByteBuf, SyncShinobiPoints> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> buf.writeInt(payload.shinobi_points),
            buf -> new SyncShinobiPoints(buf.readInt())
    );

    public SyncShinobiPoints(int shinobi_points) {
        this.shinobi_points = shinobi_points;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                context.player().getData(MainAttachment.SHINOBI_POINTS);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
