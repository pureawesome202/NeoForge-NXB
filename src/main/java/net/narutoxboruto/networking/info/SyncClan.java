package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncClan implements CustomPacketPayload {

    private String clan;

    public static final CustomPacketPayload.Type<SyncClan> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_clan"));

    // Fixed StreamCodec - properly handles String data
    public static final StreamCodec<FriendlyByteBuf, SyncClan> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.clan),
                    buf -> new SyncClan(buf.readUtf())
            );

    public SyncClan(String clan) {
        this.clan = clan;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getData(MainAttachment.CLAN).setValue(clan, serverPlayer);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
