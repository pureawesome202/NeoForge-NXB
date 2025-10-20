package net.narutoxboruto.attachments.jutsus;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.jutsus.SyncYingList;
import net.narutoxboruto.util.ModUtil;

public class YingList {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<YingList> CODEC = Codec.STRING.xmap(YingList::new, YingList::getValue);


    // Constructor for codec
    public YingList(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public YingList(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public YingList() {
        this("earth");
    }

    public Object getSyncMessage() {
        return new SyncYingList(this.value);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public void concatList(String value, ServerPlayer serverPlayer) {
        this.value = ModUtil.concatAndFormat(this.value, value);
        this.syncValue(serverPlayer);
    }

    public void copyFrom(YingList source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = "";
        this.syncValue(serverPlayer);
    }

    public void setValue(String value, ServerPlayer serverPlayer) {
        this.value = value;
        this.syncValue(serverPlayer);
    }
}
