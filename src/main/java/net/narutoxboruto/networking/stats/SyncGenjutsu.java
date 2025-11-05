package net.narutoxboruto.networking.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Clan;
import net.narutoxboruto.attachments.stats.Genjutsu;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class SyncGenjutsu implements CustomPacketPayload {
    private final int genjutsu;

    public static final Type<SyncGenjutsu> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_genjutsu"));

    public static final StreamCodec<FriendlyByteBuf, SyncGenjutsu> STREAM_CODEC = StreamCodec.ofMember(SyncGenjutsu::toBytes, SyncGenjutsu::new);

    public SyncGenjutsu(int genjutsu) {
        this.genjutsu = genjutsu;
    }

    public SyncGenjutsu(FriendlyByteBuf buf) {
        this.genjutsu = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(genjutsu);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Genjutsu genjutsu = clientPlayer.getData(MainAttachment.GENJUTSU);
                genjutsu.setValue(this.genjutsu); // Use client-side method
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
