package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncClan implements CustomPacketPayload {

    private  String clan;

    public static final CustomPacketPayload.Type<SyncClan> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_clan"));

    public static final StreamCodec<FriendlyByteBuf, SyncClan> STREAM_CODEC = StreamCodec.of((buf, string) -> buf.writeInt(Integer.parseInt(string.clan)), buf -> new SyncClan(String.valueOf(buf.readInt())));

        public SyncClan(String clan) {
            this.clan = clan;
        }

        public SyncClan(FriendlyByteBuf buf) {
            this.clan = buf.readUtf();
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.clan);
        }

        public void handle(IPayloadContext context) {
            context.enqueueWork(() -> PlayerData.setClan(clan));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {return TYPE;}
}
