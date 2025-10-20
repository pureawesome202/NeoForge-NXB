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

    public void incrementValue(int add, ServerPlayer serverPlayer, boolean awardSP) {
        int oldValue = value;
        this.value = Math.min(value + add, this.maxValue);
        this.syncValue(serverPlayer);
        if (awardSP) {
            for (int i = oldValue + 1; i <= value; i++) {
                if (i % 20 == 0) { // Replace 10 with your desired interval
                    serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(serverPlayer);
                }
            }
        }
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = add; // Removed adjustment multiplier
        this.syncValue(serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue); // Removed adjustment multiplier
        this.syncValue(serverPlayer);
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
