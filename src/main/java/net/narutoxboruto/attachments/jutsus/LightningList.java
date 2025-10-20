package net.narutoxboruto.attachments.jutsus;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.jutsus.SyncLightningList;
import net.narutoxboruto.util.ModUtil;

public class LightningList {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<LightningList> CODEC = Codec.STRING.xmap(LightningList::new, LightningList::getValue);


    // Constructor for codec
    public LightningList(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public LightningList(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public LightningList() {
        this("lightning");
    }

    public Object getSyncMessage() {
        return new SyncLightningList(this.value);
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

    public void copyFrom(LightningList source, ServerPlayer serverPlayer) {
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
