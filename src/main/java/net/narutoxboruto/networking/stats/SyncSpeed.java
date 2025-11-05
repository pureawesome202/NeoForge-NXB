package net.narutoxboruto.networking.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.Shurikenjutsu;
import net.narutoxboruto.attachments.stats.Speed;
import net.narutoxboruto.client.PlayerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncSpeed implements CustomPacketPayload {
    private final int speed;

    public static final CustomPacketPayload.Type<SyncSpeed> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_speed"));

    public static final StreamCodec<FriendlyByteBuf, SyncSpeed> STREAM_CODEC = StreamCodec.ofMember(SyncSpeed::toBytes, SyncSpeed::new);

    public SyncSpeed(int speed) {
        this.speed = speed;
    }

    public SyncSpeed(FriendlyByteBuf buf) {
        this.speed = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(speed);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null && Minecraft.getInstance().player != null) {
                LocalPlayer clientPlayer = Minecraft.getInstance().player;

                Speed speed = clientPlayer.getData(MainAttachment.SPEED);
                speed.setValue(this.speed); // Use client-side method
            }
        });
    }


    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
