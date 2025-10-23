package net.narutoxboruto.networking.info;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Supplier;

public class SyncReleaseList implements CustomPacketPayload {

    private final String releaseList;

    public static final CustomPacketPayload.Type<SyncReleaseList> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_release_list"));

    public static final StreamCodec<FriendlyByteBuf, SyncReleaseList> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, // Use string codec
            SyncReleaseList::getReleaseList,
            SyncReleaseList::new
    );
    public SyncReleaseList(String releaseList) {
        this.releaseList = releaseList;
    }

    public SyncReleaseList(FriendlyByteBuf buf) {
        this.releaseList = buf.readUtf(); // Read as plain string
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(releaseList); // Write as plain string
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> PlayerData.setReleaseList(releaseList));
    }

    public String getReleaseList() {
        return releaseList;
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {return TYPE;}
}
