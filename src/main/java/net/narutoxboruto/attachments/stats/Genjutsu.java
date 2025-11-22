package net.narutoxboruto.attachments.stats;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.stats.SyncGenjutsu;

public class Genjutsu {
    private final String id;
    protected int maxValue;
    public int value;

    public static final Codec<Genjutsu> CODEC = Codec.INT.xmap(value -> {
        Genjutsu genjutsu = new Genjutsu();
        genjutsu.value = value;
        return genjutsu;
    }, Genjutsu::getValue);

    public Genjutsu() {
        this.id = "genjutsu";
        this.maxValue = 300;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncGenjutsu(getValue()), serverPlayer);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = add; // Removed adjustment multiplier
        this.syncValue(serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue);
        this.syncValue(serverPlayer);
    }

    public void setValue(int value) {
        this.value = Math.min(value, maxValue);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0); // Removed adjustment multiplier
        this.syncValue(serverPlayer);
    }

    public void copyFrom(Genjutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
    }
}
