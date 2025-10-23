package net.narutoxboruto.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.networking.info.*;
import net.narutoxboruto.networking.jutsus.*;
import net.narutoxboruto.networking.misc.RechargeChakra;
import net.narutoxboruto.networking.misc.ToggleSwordAbility;
import net.narutoxboruto.networking.selection.*;
import net.narutoxboruto.networking.stats.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPacketHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0"); // Your network version
         //Clientbound packets
        registrar.playToClient(SyncChakra.TYPE, SyncChakra.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncMaxChakra.TYPE, SyncMaxChakra.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncAffiliation.TYPE, SyncAffiliation.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncClan.TYPE, SyncClan.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncRank.TYPE, SyncRank.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncReleaseList.TYPE, SyncReleaseList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncShinobiPoints.TYPE, SyncShinobiPoints.STREAM_CODEC, (payload, context) -> payload.handle(context));

        registrar.playToClient(SyncEarthList.TYPE, SyncEarthList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncFireList.TYPE, SyncFireList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncLightningList.TYPE, SyncLightningList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncWindList.TYPE, SyncWindList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncWaterList.TYPE, SyncWaterList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncYangList.TYPE, SyncYangList.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncYingList.TYPE, SyncYingList.STREAM_CODEC, (payload, context) -> payload.handle(context));

        registrar.playToClient(SyncSelectionEarth.TYPE, SyncSelectionEarth.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionFire.TYPE, SyncSelectionFire.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionLightning.TYPE, SyncSelectionLightning.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionRelease.TYPE, SyncSelectionRelease.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionWater.TYPE, SyncSelectionWater.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionWind.TYPE, SyncSelectionWind.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionYang.TYPE, SyncSelectionYang.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToClient(SyncSelectionYin.TYPE, SyncSelectionYin.STREAM_CODEC, (payload, context) -> payload.handle(context));

        //Serverbound packets
        registrar.playToServer(ToggleSwordAbility.TYPE, ToggleSwordAbility.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(RechargeChakra.TYPE, RechargeChakra.STREAM_CODEC, (payload, context) -> payload.handle(context));

        registrar.playToServer(SyncGenjutsu.TYPE, SyncGenjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncKenjutsu.TYPE, SyncKenjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncKinjutsu.TYPE, SyncKinjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncMedical.TYPE, SyncMedical.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncNinjutsu.TYPE, SyncNinjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncSenjutsu.TYPE, SyncSenjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncShurikenjutsu.TYPE, SyncShurikenjutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncSummoning.TYPE, SyncSummoning.STREAM_CODEC, (payload, context) -> payload.handle(context));
        registrar.playToServer(SyncTaijutsu.TYPE, SyncTaijutsu.STREAM_CODEC, (payload, context) -> payload.handle(context));




    }

    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }
}
