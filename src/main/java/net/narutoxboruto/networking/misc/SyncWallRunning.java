package net.narutoxboruto.networking.misc;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.client.PlayerData;
import net.narutoxboruto.main.Main;
import net.narutoxboruto.util.RotationUtil;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Network packet to synchronize wall running state from server to client.
 */
public record SyncWallRunning(RotationUtil.Surface surface) implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<SyncWallRunning> TYPE = 
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "sync_wall_running"));
    
    public static final StreamCodec<ByteBuf, SyncWallRunning> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            packet -> packet.surface.ordinal(),
            ordinal -> new SyncWallRunning(RotationUtil.Surface.values()[ordinal])
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handle(SyncWallRunning packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Update client-side cache
            PlayerData.setWallRunningSurface(packet.surface);
        });
    }
}