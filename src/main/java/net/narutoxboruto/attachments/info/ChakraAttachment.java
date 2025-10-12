package net.narutoxboruto.attachments.info;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

// Your data class, now suitable for use as an attachment
public class ChakraAttachment implements INBTSerializable<CompoundTag> {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addValue(int add, int maxChakra, ServerPlayer pTarget) {
        this.value = Math.min(this.value + add, maxChakra);
    }

    public void subValue(int sub) {
        this.value = Math.max(this.value - sub, 0);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("chakra", value);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.value = nbt.getInt("chakra");
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(this.value - sub, 0);
    }
}
