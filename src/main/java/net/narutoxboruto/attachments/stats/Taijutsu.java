package net.narutoxboruto.attachments.stats;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.stats.SyncTaijutsu;

public class Taijutsu {

    private final String id;
    protected int maxValue;
    protected int value;

    public static final Codec<Taijutsu> CODEC = Codec.INT.xmap(value -> {Taijutsu taijutsu = new Taijutsu();taijutsu.value = value;return taijutsu;}, Taijutsu::getValue);

    public Taijutsu() {
        this.id = "taijutsu";
        this.maxValue = 500;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncTaijutsu(getValue()), serverPlayer);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);

        // Always award SP when Taijutsu increases
        serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(add, serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = add;
        this.syncValue(serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue);
        this.syncValue(serverPlayer);
    }

    public void setValue(int value) {
        this.value = Math.min(value, maxValue); // Removed adjustment multiplier
    }


    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
    }

    public void copyFrom(Taijutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
    }
}
