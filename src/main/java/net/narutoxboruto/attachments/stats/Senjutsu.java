package net.narutoxboruto.attachments.stats;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.stats.SyncSenjutsu;

public class Senjutsu {
    private final String id;
    protected int maxValue;
    protected int value;
    private int adjustment;



    public static final Codec<Senjutsu> CODEC = Codec.INT.xmap(value -> {Senjutsu senjutsu = new Senjutsu();senjutsu.value = value;return senjutsu;}, Senjutsu::getValue);

    public Senjutsu() {
        this.id = "senjutsu";
        this.maxValue = 500;
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncSenjutsu(getValue()), serverPlayer);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);

        // Always award SP
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

    public void copyFrom(Senjutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
    }

}
