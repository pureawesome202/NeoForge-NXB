package net.narutoxboruto.attachments.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.entities.ModeHandler;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.misc.SyncNarutoRun;

public class NarutoRun {
    private boolean value;

    public static final Codec<NarutoRun> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("value").forGetter(NarutoRun::isActive)
            ).apply(instance, NarutoRun::new)
    );

    // Constructor for Codec
    public NarutoRun(boolean value) {
        this.value = value;
    }

    // Default constructor
    public NarutoRun() {
        this(false);
    }

    public boolean isActive() {
        return value;
    }

    public void setValue(boolean b, ServerPlayer player) {
        this.value = b;
        this.syncValue(player);
        ((ModeHandler) player).$setNarutoRunning(b);
    }

    public void syncValue(ServerPlayer player) {
        ModPacketHandler.sendToPlayer(new SyncNarutoRun(this.value), player);
    }

    public void copyFrom(NarutoRun source, ServerPlayer serverPlayer) {
        this.value = source.isActive();
        this.syncValue(serverPlayer);
    }

    // Getter for serialization
    public boolean getValue() {
        return value;
    }
}