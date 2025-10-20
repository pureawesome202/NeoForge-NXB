package net.narutoxboruto.attachments.jutsus;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.jutsus.SyncEarthList;
import net.narutoxboruto.util.ModUtil;

public class EarthList {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<EarthList> CODEC = Codec.STRING.xmap(EarthList::new, EarthList::getValue);


    // Constructor for codec
    public EarthList(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public EarthList(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public EarthList() {
        this("earth");
    }

    public Object getSyncMessage() {
        return new SyncEarthList(this.value);
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

    public void copyFrom(EarthList source, ServerPlayer serverPlayer) {
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
