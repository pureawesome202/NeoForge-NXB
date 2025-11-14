package net.narutoxboruto.attachments.stats;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.stats.SyncNinjutsu;

public class Ninjutsu {

    private final String id;
    protected int maxValue;
    protected int value;

    public static final Codec<Ninjutsu> CODEC = Codec.INT.xmap(value -> {Ninjutsu ninjutsu = new Ninjutsu();ninjutsu.value = value;return ninjutsu;}, Ninjutsu::getValue);

    public Ninjutsu() {
        this.id = "ninjutsu";
        this.maxValue = 500;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncNinjutsu(getValue()), serverPlayer);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);

        // Always award SP
        serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(add, serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = add; // Removed adjustment multiplier
        this.syncValue(serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue); // Removed adjustment multiplier
        this.syncValue(serverPlayer);

    }

    public void setValue(int value) {
        this.value = Math.min(value, maxValue);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0); // Removed adjustment multiplier
        this.syncValue(serverPlayer);
    }

    public void copyFrom(Ninjutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
    }
}
