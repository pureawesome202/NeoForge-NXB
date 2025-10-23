package net.narutoxboruto.attachments.selection;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.jutsus.EarthList;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.selection.SyncSelectionLightning;
import net.narutoxboruto.util.ModUtil;

public class SelectedLightning {
    private final String id;
    protected String value = "";

    // Codec for serialization
    public static final Codec<SelectedLightning> CODEC = Codec.STRING.xmap(SelectedLightning::new, SelectedLightning::getValue);


    // Constructor for codec
    public SelectedLightning(String id, String value) {
        this.id = id;
        this.value = value;
    }

    // Default constructor
    public SelectedLightning(String identifier) {
        this.id = identifier;
        this.value = "";
    }

    public SelectedLightning() {
        this("selectedlightning");
    }

    public Object getSyncMessage() {
        return new SyncSelectionLightning(this.value);
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
