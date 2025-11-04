package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncAffiliation implements CustomPacketPayload {
    private final String affiliation;

    public static final CustomPacketPayload.Type<SyncAffiliation> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_affiliation"));

    // Fixed StreamCodec - properly handles String data
    public static final StreamCodec<FriendlyByteBuf, SyncAffiliation> STREAM_CODEC =
            StreamCodec.of(
                    (buf, packet) -> buf.writeUtf(packet.affiliation),
                    buf -> new SyncAffiliation(buf.readUtf())
            );

    public SyncAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getData(MainAttachment.AFFILIATION).setValue(affiliation, serverPlayer);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
