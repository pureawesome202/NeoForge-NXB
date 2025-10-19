package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.info.SyncRank;
import net.narutoxboruto.networking.info.SyncReleaseList;
import net.narutoxboruto.networking.info.SyncShinobiPoints;

public class ShinobiPoints {
    private int value;

    public static final Codec<ShinobiPoints> CODEC = Codec.INT.xmap(ShinobiPoints::new, ShinobiPoints::getValue);

    public ShinobiPoints() {
        this.value = Integer.MAX_VALUE;
    }

    public ShinobiPoints(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.incrementValue(1, serverPlayer);
    }


    public Object getSyncMessage() {
        return new SyncRank(String.valueOf(getValue()));
    }
}
