package net.narutoxboruto.capabilities.info;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncChakra;

public class Chakra {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addValue(int add, int maxChakra, ServerPlayer serverPlayer) {
        this.value = Math.min(this.value + add, maxChakra);
        this.syncValue(serverPlayer);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        if (!serverPlayer.getAbilities().instabuild) {
            this.value = Math.max(this.value - sub, 0);
            this.syncValue(serverPlayer);
        }
    }

    public void syncValue(ServerPlayer serverPlayer) {
        // Create a fresh packet each time
        SyncChakra packet = new SyncChakra(this.value);
        ModPacketHandler.sendToPlayer(packet, serverPlayer);
    }

    public void reset(int maxChakra, ServerPlayer serverPlayer) {
        this.value = maxChakra / 2;
        this.syncValue(serverPlayer);
    }

    public void replenish(int maxChakra, ServerPlayer serverPlayer) {
        this.value = maxChakra;
        this.syncValue(serverPlayer);
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("chakra", value);
    }

    public void loadNBTData(CompoundTag nbt) {
        this.value = nbt.getInt("chakra");
    }
}

