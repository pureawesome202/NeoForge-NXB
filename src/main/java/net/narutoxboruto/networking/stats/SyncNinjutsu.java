package net.narutoxboruto.networking.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.Genjutsu;
import net.narutoxboruto.attachments.stats.Ninjutsu;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncNinjutsu implements CustomPacketPayload {
    private final int ninjutsu;

    public static final CustomPacketPayload.Type<SyncNinjutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_ninjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncNinjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncNinjutsu::toBytes, SyncNinjutsu::new);

    public SyncNinjutsu(int ninjutsu) {
        this.ninjutsu = ninjutsu;
    }

    public SyncNinjutsu(FriendlyByteBuf buf) {
        this.ninjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(ninjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Ninjutsu ninjutsu = clientPlayer.getData(MainAttachment.NINJUTSU);
                ninjutsu.setValue(this.ninjutsu); // Use client-side method
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
