package net.narutoxboruto.networking.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.modes.NarutoRun;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SyncNarutoRun implements CustomPacketPayload {

    private final boolean value;

    public static final Type<SyncNarutoRun> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "sync_naruto_run"));

    public static final StreamCodec<FriendlyByteBuf, SyncNarutoRun> STREAM_CODEC = StreamCodec.ofMember(SyncNarutoRun::toBytes, SyncNarutoRun::new);

    public SyncNarutoRun(boolean value) {
        this.value = value;
    }

    public SyncNarutoRun(FriendlyByteBuf buf) {
        this.value = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(value);
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            context.player().getData(MainAttachment.NARUTO_RUN);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
