package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.narutoxboruto.networking.ModPacketHandler;
import net.narutoxboruto.networking.info.SyncReleaseList;
import net.narutoxboruto.util.ModUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReleaseList {
    private final String id;
    protected String value = "";

    public static final Codec<ReleaseList> CODEC = Codec.STRING.xmap(
            value -> {
                ReleaseList releaseList = new ReleaseList();
                releaseList.value = value;
                return releaseList;
            },
            releaseList -> releaseList.getValue()
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

    public void syncValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
        }
    }

    public String getValue() {
        return value;
    }

    public void concatList(String newRelease, Player player) {
        if (value.isEmpty()) {
            value = newRelease;
        } else {
            value = value + ", " + newRelease;
        }

        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void copyFrom(ReleaseList source, Player player) {
        this.value = source.getValue();
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void resetValue(Player player) {
        this.value = "";
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void setValue(String value, Player player) {
        this.value = value;
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    // Client-side only method
    public void setValue(String value) {
        this.value = value;
    }

    // Helper method to get releases as a list
    public List<String> getReleasesAsList() {
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        // Split by comma and trim whitespace
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    // Check if the release list is empty
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }
}
