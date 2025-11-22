package net.narutoxboruto.networking.jutsus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.ReleaseList;
import net.narutoxboruto.attachments.jutsus.EarthList;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncEarthList implements CustomPacketPayload {
    private final String earthList;

    public static final CustomPacketPayload.Type<SyncEarthList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_earth_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncEarthList> STREAM_CODEC = StreamCodec.ofMember(SyncEarthList::toBytes, SyncEarthList::new);

    public SyncEarthList(String earthList) { this.earthList = earthList; }

    public SyncEarthList(FriendlyByteBuf buf) {
        this.earthList = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(earthList);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                EarthList releaseListAttachment = player.getData(MainAttachment.EARTHLIST);
                releaseListAttachment.setValue(this.earthList);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
