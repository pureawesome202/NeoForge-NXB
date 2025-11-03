package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSenjutsu implements CustomPacketPayload {
    private final int senjutsu;

    public static final CustomPacketPayload.Type<SyncSenjutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_senjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncSenjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncSenjutsu::toBytes, SyncSenjutsu::new);

    public SyncSenjutsu(int senjutsu) {
        this.senjutsu = senjutsu;
    }

    public SyncSenjutsu(FriendlyByteBuf buf) {
        this.senjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(senjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                context.player().getData(MainAttachment.SENJUTSU);
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
