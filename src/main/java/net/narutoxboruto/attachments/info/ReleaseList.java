package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncReleaseList;
import net.narutoxboruto.util.ModUtil;

import java.util.Collections;
import java.util.List;

public class ReleaseList {
    private final String id;
    protected String value = "";

    public static final Codec<ReleaseList> CODEC = Codec.STRING.xmap(
            value -> {
                System.out.println("Deserializing ReleaseList with value: " + value); // Debug
                ReleaseList releaseList = new ReleaseList();
                releaseList.value = value;
                return releaseList;
            },
            releaseList -> {
                System.out.println("Serializing ReleaseList with value: " + releaseList.value); // Debug
                return releaseList.getValue();
            }
    );

    public ReleaseList(String identifier) {
        id = identifier;
    }

    public ReleaseList() {
        this("releaseList");
    }

    public Object getSyncMessage() {
        return new SyncReleaseList(getValue());
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
    }

    public String getValue() {
        return value;
    }

    public void concatList(String newRelease, ServerPlayer serverPlayer) {
        List<String> list = new java.util.ArrayList<>(
                ModUtil.getArrayFrom(ModUtil.concatAndFormat(getValue(), newRelease)));
        Collections.sort(list);
        String s = String.valueOf(list);
        this.value = s.substring(1, s.length() - 1);
        this.syncValue(serverPlayer);
    }

    public void copyFrom(ReleaseList source, ServerPlayer serverPlayer) {
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
