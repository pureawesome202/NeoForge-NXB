package net.narutoxboruto.attachments.stats;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
        updateHandDamage(serverPlayer);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = add;
        this.syncValue(serverPlayer);
        updateHandDamage(serverPlayer);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue);
        this.syncValue(serverPlayer);
        updateHandDamage(serverPlayer);
    }

    public void setValue(int value) {
        this.value = Math.min(value, maxValue);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
        updateHandDamage(serverPlayer);
    }

    public void copyFrom(Taijutsu source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
        updateHandDamage(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.syncValue(serverPlayer);
        updateHandDamage(serverPlayer);
    }

    private void updateHandDamage(ServerPlayer serverPlayer) {
        AttributeInstance attackDamageAttr = serverPlayer.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamageAttr != null) {
            // Calculate hand damage: 1 at 0 value, 15 at 500 value
            // Linear scaling: damage = 1 + (value / 500) * 14
            double handDamage = 1.0 + (this.value / 500.0) * 14.0;
            attackDamageAttr.setBaseValue(handDamage);
        }
    }
}
