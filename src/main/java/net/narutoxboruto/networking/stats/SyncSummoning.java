package net.narutoxboruto.networking.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.Speed;
import net.narutoxboruto.attachments.stats.Summoning;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSummoning implements CustomPacketPayload {
    private final int summoning;

    public static final CustomPacketPayload.Type<SyncSummoning> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_summoning"));

    public static final StreamCodec<FriendlyByteBuf, SyncSummoning> STREAM_CODEC = StreamCodec.ofMember(SyncSummoning::toBytes, SyncSummoning::new);

    public SyncSummoning(int summoning) {
        this.summoning = summoning;
    }

    public SyncSummoning(FriendlyByteBuf buf) {
        this.summoning = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(summoning);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Summoning summoning = clientPlayer.getData(MainAttachment.SUMMONING);
                summoning.setValue(this.summoning); // Use client-side method
            }
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
