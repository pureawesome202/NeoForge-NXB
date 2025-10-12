package net.narutoxboruto.attachments.info;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class MaxChakraAttachment implements INBTSerializable<CompoundTag> {
    private int value = 10;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.value = this.value + add;
        this.syncValue(serverPlayer);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        // Use the new attachment sync system instead of manual packets
        serverPlayer.syncData(MainAttachment.MAX_CHAKRA.get());
    }

    public void copyFrom(MaxChakraAttachment source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("maxChakra", value);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        value = nbt.getInt("maxChakra");
    }
}
