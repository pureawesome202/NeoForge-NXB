package net.narutoxboruto.networking.info;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Affiliation;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncAffiliation implements CustomPacketPayload {
    private final String affiliation;

    public static final CustomPacketPayload.Type<SyncAffiliation> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_affiliation"));

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
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                // Get the existing Affiliation object and update its value for client side only
                Affiliation affiliationObj = clientPlayer.getData(MainAttachment.AFFILIATION);
                affiliationObj.setValue(this.affiliation); // Use client-side method
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
