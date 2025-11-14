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

        // Always award SP
        serverPlayer.getData(MainAttachment.SHINOBI_POINTS).incrementValue(add, serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        int oldValue = this.value;
        this.value = Math.min(this.value + add, maxValue); // Fixed: now actually adds instead of setting

        if (this.value != oldValue) {
            this.syncValue(serverPlayer);
        }
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        int oldValue = this.value;
        this.value = Math.min(value, maxValue);

        if (this.value != oldValue) {
            this.syncValue(serverPlayer);
        }
    }

    public void setValue(int value) {
        this.value = Math.min(value, maxValue);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        int oldValue = this.value;
        this.value = Math.max(value - sub, 0);

        if (this.value != oldValue) {
            this.syncValue(serverPlayer);
        }
    }

    public void copyFrom(Genjutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        int oldValue = this.value;
        this.value = 0;

        if (oldValue != 0) {
            this.syncValue(serverPlayer);
        }
    }
}
