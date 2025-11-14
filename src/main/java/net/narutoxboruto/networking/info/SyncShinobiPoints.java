package net.narutoxboruto.networking.info;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.ShinobiPoints;
import net.narutoxboruto.attachments.stats.Genjutsu;
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
                ShinobiPoints points = context.player().getData(MainAttachment.SHINOBI_POINTS);

                // Update the value directly (you might need to make 'value' field accessible)
                points.value = Math.min(this.shinobi_points, points.getMaxValue());}
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
