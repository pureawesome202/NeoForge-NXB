package net.narutoxboruto.attachments.info;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncReleaseList;
import net.narutoxboruto.util.ModUtil;
import com.mojang.serialization.Codec;


import java.util.Collections;
import java.util.List;

import static net.narutoxboruto.attachments.MainAttachment.SHINOBI_POINTS;

public class ReleaseList {
    private final String id;
    protected int maxValue;
    protected int value;
    protected String stringValue;

    public static final Codec<ReleaseList> CODEC = Codec.STRING.xmap(ReleaseList::new, ReleaseList::getStringValue);

    public ReleaseList() {
        this.id = "releaseList";
        this.maxValue = Integer.MAX_VALUE;
        this.stringValue = "";
    }

    public ReleaseList(String id, int max_value) {
        this.id = id;
        this.maxValue = max_value;
        this.stringValue = "";
    }

    // Codec constructor - takes string value
    public ReleaseList(String stringValue) {
        this.id = "releaseList";
        this.maxValue = Integer.MAX_VALUE;
        this.stringValue = stringValue != null ? stringValue : "";
    }

    // Integer value methods
    public int getValue() {
        return value;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Object getSyncMessage() {
        return new SyncReleaseList(stringValue);
    }

    public void incrementValue(int add, ServerPlayer serverPlayer, boolean awardSP) {
        this.value = Math.min(value + add, maxValue);
        this.syncValue(serverPlayer);
        if (awardSP) {
            ShinobiPoints shinobiPoints = serverPlayer.getData(SHINOBI_POINTS);
            shinobiPoints.incrementValue(add, serverPlayer);
        }
    }

    public void incrementValue(int add, ServerPlayer serverPlayer) {
        this.incrementValue(add, serverPlayer, false);
    }

    public void addValue(int add, ServerPlayer serverPlayer) {
        this.incrementValue(add, serverPlayer, false);
    }

    public void setValue(int value, ServerPlayer serverPlayer) {
        this.value = Math.min(value, maxValue);
        this.syncValue(serverPlayer);
    }

    public void subValue(int sub, ServerPlayer serverPlayer) {
        this.value = Math.max(value - sub, 0);
        this.syncValue(serverPlayer);
    }

    public void syncValue(ServerPlayer serverPlayer) {
        ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
    }

    public void copyFrom(ReleaseList source, ServerPlayer serverPlayer) {
        this.value = source.getValue();
        this.stringValue = source.getStringValue();
        this.syncValue(serverPlayer);
    }

    public void resetValue(ServerPlayer serverPlayer) {
        this.value = 0;
        this.stringValue = "";
        this.syncValue(serverPlayer);
    }

    public void concatList(String newRelease, ServerPlayer serverPlayer) {
        List<String> list = new java.util.ArrayList<>(
                ModUtil.getArrayFrom(ModUtil.concatAndFormat(stringValue, newRelease)));
        Collections.sort(list);
        String s = String.valueOf(list);
        this.stringValue = s.substring(1, s.length() - 1);
        this.syncValue(serverPlayer);
    }

    public void setStringValue(String value, ServerPlayer serverPlayer) {
        this.stringValue = value;
        this.syncValue(serverPlayer);
    }
}
