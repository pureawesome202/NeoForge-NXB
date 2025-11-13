package net.narutoxboruto.attachments.modes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.entities.ModeHandler;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.misc.SyncChakraControl;

public class ChakraControl {
    private boolean value;

    public static final Codec<ChakraControl> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.fieldOf("value").forGetter(ChakraControl::isActive)
            ).apply(instance, ChakraControl::new)
    );

    // Constructor for Codec
    public ChakraControl(boolean value) {
        this.value = value;
    }

    // Default constructor
    public ChakraControl() {
        this(false);
    }

    public boolean isActive() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(boolean b, ServerPlayer serverPlayer) {
        this.value = b;
        this.syncValue(serverPlayer);
        ((ModeHandler) serverPlayer).$setChakraControl(b);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncChakraControl(this.value), serverPlayer);
    }

    public void copyFrom(ChakraControl source, ServerPlayer serverPlayer) {
        this.value = source.isActive();
        this.syncValue(serverPlayer);
    }

    // Getter for serialization
    public boolean getValue() {
        return value;
    }
}
