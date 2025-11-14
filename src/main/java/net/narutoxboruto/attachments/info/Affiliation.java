package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncAffiliation;
import net.narutoxboruto.util.ModUtil;

public class Affiliation {
    private String value;

    public static final Codec<Affiliation> CODEC = Codec.STRING.xmap(Affiliation::new, Affiliation::getValue);


    public Affiliation() {
        this.value = "affiliation";
    }

    public Affiliation(String value) {
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
        ModPacketHandler.sendToPlayer(new SyncAffiliation(getValue()), serverPlayer);
    }
}
