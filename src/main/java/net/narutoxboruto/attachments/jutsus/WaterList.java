package net.narutoxboruto.attachments.jutsus;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.jutsus.SyncWaterList;
import net.narutoxboruto.util.ModUtil;

public class WaterList {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<WaterList> CODEC = Codec.STRING.xmap(WaterList::new, WaterList::getValue);


    // Constructor for codec
    public WaterList(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public WaterList(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public WaterList() {
        this("water");
    }

    public Object getSyncMessage() {
        return new SyncWaterList(this.value);
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

    public void copyFrom(WaterList source, ServerPlayer serverPlayer) {
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
