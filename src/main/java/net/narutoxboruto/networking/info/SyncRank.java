package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncRank implements CustomPacketPayload {
    private String rank;

    public static final CustomPacketPayload.Type<SyncRank> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_rank"));

    public static final StreamCodec<FriendlyByteBuf, SyncRank> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.rank),
                    buf -> new SyncRank(buf.readUtf())
            );

    public SyncRank(String rank) {
        this.rank = rank;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getData(MainAttachment.RANK).setValue(rank, serverPlayer);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
