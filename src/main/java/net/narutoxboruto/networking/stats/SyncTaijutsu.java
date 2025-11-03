package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncTaijutsu implements CustomPacketPayload {
    private final int taijutsu;

    public static final CustomPacketPayload.Type<SyncTaijutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_taijutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncTaijutsu> STREAM_CODEC = StreamCodec.ofMember(SyncTaijutsu::toBytes, SyncTaijutsu::new);

    public SyncTaijutsu(int taijutsu) {
        this.taijutsu = taijutsu;
    }

    public SyncTaijutsu(FriendlyByteBuf buf) {
        this.taijutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(taijutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                context.player().getData(MainAttachment.TAIJUTSU);
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
