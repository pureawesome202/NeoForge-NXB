package net.narutoxboruto.networking.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.narutoxboruto.items.swords.AbstractAbilitySword;
import net.narutoxboruto.networking.info.SyncMaxChakra;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleSwordAbility implements CustomPacketPayload {

    public static final Type<ToggleSwordAbility> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "toggle_sword_ability"));

    public static final StreamCodec<FriendlyByteBuf, ToggleSwordAbility> STREAM_CODEC = StreamCodec.ofMember(ToggleSwordAbility::toBytes, ToggleSwordAbility::new);

    public ToggleSwordAbility() {
        // Empty constructor for creation
    }

    // Proper deserialization constructor
    public ToggleSwordAbility(FriendlyByteBuf buf) {
        // Add any data reading here if your packet needs to send data
    }

    // Proper serialization method
    public void toBytes(FriendlyByteBuf buf) {
        // Add any data writing here if your packet needs to send data
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer serverPlayer = (ServerPlayer) context.player();
            ((AbstractAbilitySword) serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem()).toggleAbility(serverPlayer);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}