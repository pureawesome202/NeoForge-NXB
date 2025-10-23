package net.narutoxboruto.attachments.selection;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.jutsus.EarthList;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.selection.SyncSelectionEarth;
import net.narutoxboruto.util.ModUtil;

public class SelectedEarth {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<SelectedEarth> CODEC = Codec.STRING.xmap(SelectedEarth::new, SelectedEarth::getValue);


    // Constructor for codec
    public SelectedEarth(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public SelectedEarth(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public SelectedEarth() {
        this("earth");
    }

    public Object getSyncMessage() {
        return new SyncSelectionEarth(this.value);
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
