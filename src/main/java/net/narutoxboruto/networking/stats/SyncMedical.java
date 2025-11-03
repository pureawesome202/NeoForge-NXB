package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
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
            if (context.player() != null) {
                context.player().getData(MainAttachment.MEDICAL);
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
