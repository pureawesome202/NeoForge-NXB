package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncShinobiPoints;

public class ShinobiPoints {
    private final String id;
    protected int maxValue;
    public int value;

    // Codec for serialization
    public static final Codec<ShinobiPoints> CODEC = Codec.INT.xmap(value -> {ShinobiPoints shinobiPoints = new ShinobiPoints();shinobiPoints.value = value;return shinobiPoints;}, ShinobiPoints::getValue);

    // Default constructor
    public ShinobiPoints(String id, int maxValue) {
        this.id = id;
        this.maxValue = maxValue;
        this.value = 0;
    }

    // Convenience constructor
    public ShinobiPoints() {
        this("shinobi_points", Integer.MAX_VALUE);
    }

    public Object getSyncMessage() {
        return new SyncShinobiPoints(this.value);
    }

    public int getValue() {
        return value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public String getId() {
        return id;
    }

    public void incrementValue(ServerPlayer serverPlayer) {
        this.incrementValue(1, serverPlayer);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.incrementValue(add, serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue);
        this.syncValue(serverPlayer);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
    }

    public void copyFrom(ShinobiPoints source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
    }
}
