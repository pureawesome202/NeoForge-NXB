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
                System.out.println("DEBUG: Deserializing ReleaseList with value: " + value);
                ReleaseList releaseList = new ReleaseList();
                releaseList.value = value;
                return releaseList;
            },
            releaseList -> {
                System.out.println("DEBUG: Serializing ReleaseList with value: " + releaseList.value);
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

    public void syncValue(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            System.out.println("DEBUG: Sending sync packet to client: " + this.value);
            ModPacketHandler.sendToPlayer((CustomPacketPayload) getSyncMessage(), serverPlayer);
        } else {
            System.out.println("DEBUG: syncValue called but player is not ServerPlayer");
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
        System.out.println("DEBUG: concatList - new value: " + value);

        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void copyFrom(ReleaseList source, Player player) {
        this.value = source.getValue();
        System.out.println("DEBUG: copyFrom - new value: " + value);
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void resetValue(Player player) {
        this.value = "";
        System.out.println("DEBUG: resetValue");
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    public void setValue(String value, Player player) {
        this.value = value;
        System.out.println("DEBUG: setValue with player - new value: " + value);
        if (player instanceof ServerPlayer) {
            this.syncValue(player);
        }
    }

    // Client-side only method
    public void setValue(String value) {
        System.out.println("DEBUG: setValue (client) - new value: " + value);
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
