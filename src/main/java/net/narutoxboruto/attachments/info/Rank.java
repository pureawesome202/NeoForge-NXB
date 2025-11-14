package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncRank;
import net.narutoxboruto.util.ModUtil;

public class Rank {
    private String value;

    public static final Codec<Rank> CODEC = Codec.STRING.xmap(Rank::new, Rank::getValue);

    public Rank() {
        this.value = "rank";
    }

    public Rank(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value, ServerPlayer serverPlayer) {
        this.value = value;
        this.syncValue(serverPlayer);
    }
    public void setValue(String value) {
        this.value = value;
    }

    public void concatList(String value, ServerPlayer serverPlayer) {
        this.value = ModUtil.concatAndFormat(this.value, value);
        this.syncValue(serverPlayer);
    }

    public void  syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncRank(getValue()), serverPlayer);
    }
}