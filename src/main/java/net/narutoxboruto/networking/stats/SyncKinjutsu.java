package net.narutoxboruto.networking.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.Kenjutsu;
import net.narutoxboruto.attachments.stats.Kinjutsu;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncKinjutsu implements CustomPacketPayload {
    private final int kinjutsu;

    public static final CustomPacketPayload.Type<SyncKinjutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_kinjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncKinjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncKinjutsu::toBytes, SyncKinjutsu::new);

    public SyncKinjutsu(int kinjutsu) {
        this.kinjutsu = kinjutsu;
    }

    public SyncKinjutsu(FriendlyByteBuf buf) {
        this.kinjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(kinjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Kinjutsu kinjutsu = clientPlayer.getData(MainAttachment.KINJUTSU);
                kinjutsu.setValue(this.kinjutsu); // Use client-side method
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
