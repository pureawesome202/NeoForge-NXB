package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncClan;
import net.narutoxboruto.util.ModUtil;

public class Clan {
    private String value;

    public static final Codec<Clan> CODEC = Codec.STRING.xmap(Clan::new, Clan::getValue);


    public Clan() {
        this.value = "clan";
    }

    public Clan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value, ServerPlayer serverPlayer) {
        this.value = value;
        this.syncValue(serverPlayer);
    }

    public void concatList(String value, ServerPlayer serverPlayer) {
        this.value = ModUtil.concatAndFormat(this.value, value);
        this.syncValue(serverPlayer);
    }

    public void  syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer(new SyncClan(getValue()), serverPlayer);
    }
}
