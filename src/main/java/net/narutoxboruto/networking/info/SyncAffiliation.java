package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncAffiliation implements CustomPacketPayload {
    private final String affiliation;

    public static final CustomPacketPayload.Type<SyncAffiliation> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_affiliation"));

    public static final StreamCodec<FriendlyByteBuf, SyncAffiliation> STREAM_CODEC = StreamCodec.of((buf, string) -> buf.writeInt(Integer.parseInt(string.affiliation)), buf -> new SyncAffiliation(String.valueOf(buf.readInt())));

    public SyncAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public SyncAffiliation(FriendlyByteBuf buf) {
        this.affiliation = buf.readUtf();    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.affiliation);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setAffiliation(affiliation));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {return TYPE;}
}
