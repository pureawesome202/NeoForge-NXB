package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncKenjutsu implements CustomPacketPayload {
    private final int kenjutsu;

    public static final CustomPacketPayload.Type<SyncKenjutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_kenjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncKenjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncKenjutsu::toBytes, SyncKenjutsu::new);

    public SyncKenjutsu(int kenjutsu) {
        this.kenjutsu = kenjutsu;
    }

    public SyncKenjutsu(FriendlyByteBuf buf) {
        this.kenjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(kenjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setKenjutsu(kenjutsu));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
