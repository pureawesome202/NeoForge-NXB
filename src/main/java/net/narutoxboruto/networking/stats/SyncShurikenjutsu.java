package net.narutoxboruto.networking.stats;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncShurikenjutsu implements CustomPacketPayload {
    private final int shurikenjutsu;

    public static final CustomPacketPayload.Type<SyncShurikenjutsu> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_shurikenjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncShurikenjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncShurikenjutsu::toBytes, SyncShurikenjutsu::new);

    public SyncShurikenjutsu(int shurikenjutsu) {
        this.shurikenjutsu = shurikenjutsu;
    }

    public SyncShurikenjutsu(FriendlyByteBuf buf) {
        this.shurikenjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(shurikenjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setShurikenjutsu(shurikenjutsu));
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
