package net.narutoxboruto.attachments.jutsus;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.jutsus.SyncFireList;
import net.narutoxboruto.util.ModUtil;

public class FireList {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<FireList> CODEC = Codec.STRING.xmap(FireList::new, FireList::getValue);


    // Constructor for codec
    public FireList(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public FireList(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public FireList() {
        this("fire");
    }

    public Object getSyncMessage() {
        return new SyncFireList(this.value);
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

    public void copyFrom(FireList source, ServerPlayer serverPlayer) {
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

    public void setValue(String value) {
        this.value = value;

    }
}
