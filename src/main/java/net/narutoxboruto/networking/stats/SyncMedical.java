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
import net.narutoxboruto.attachments.stats.Medical;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncMedical implements CustomPacketPayload {
    private final int medical;

    public static final CustomPacketPayload.Type<SyncMedical> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_medical"));

    public static final StreamCodec<FriendlyByteBuf, SyncMedical> STREAM_CODEC = StreamCodec.ofMember(SyncMedical::toBytes, SyncMedical::new);

    public SyncMedical(int medical) {
        this.medical = medical;
    }

    public SyncMedical(FriendlyByteBuf buf) {
        this.medical = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(medical);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Medical medical = clientPlayer.getData(MainAttachment.MEDICAL);
                medical.setValue(this.medical); // Use client-side method
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
