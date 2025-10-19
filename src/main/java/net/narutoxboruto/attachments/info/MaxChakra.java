package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncMaxChakra;

public class MaxChakra {
    private int value = 10;

    public static final Codec<MaxChakra> CODEC = Codec.INT.xmap(MaxChakra::new, MaxChakra::getValue);

    public MaxChakra(int value) {
        this.value = value;
    }

    public MaxChakra() {
    }

    public int getValue() {
        return value;
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = this.value + add;
        this.syncValue(serverPlayer);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncMaxChakra(getValue()), serverPlayer);
    }
    public void copyFrom(MaxChakra source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }
}
