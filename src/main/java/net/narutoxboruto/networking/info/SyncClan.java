package net.narutoxboruto.networking.info;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Clan;
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
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Clan clan = clientPlayer.getData(MainAttachment.CLAN);
                clan.setValue(this.clan); // Use client-side method
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
