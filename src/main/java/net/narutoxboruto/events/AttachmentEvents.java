package net.narutoxboruto.events;

import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.main.Main;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


@Mod(Main.MOD_ID)
public class AttachmentEvents {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            System.out.println("DEBUG: Player logged in, release list: '" +
                    serverPlayer.getData(MainAttachment.RELEASE_LIST).getValue() + "'");
        }
    }

    @SubscribeEvent
    public static void onJoinWorldSyncCap(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
           // serverPlayer.getCapability(ModeCapabilityProvider.CHAKRA_CONTROL).ifPresent(
           //         chakraControl -> chakraControl.syncValue(serverPlayer));
            //INFO
            serverPlayer.getData(MainAttachment.AFFILIATION).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.CLAN).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.CHAKRA).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.SHINOBI_POINTS).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.RELEASE_LIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.RANK).syncValue(serverPlayer);
            //STATS
            serverPlayer.getData(MainAttachment.GENJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.KENJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.KINJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.MEDICAL).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.NINJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.SENJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.SHURIKENJUTSU).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.SPEED).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.SUMMONING).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.TAIJUTSU).syncValue(serverPlayer);


            //JUTSU_LIST
            serverPlayer.getData(MainAttachment.FIRELIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.EARTHLIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.WATERLIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.WINDLIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.LIGHTINGLIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.YANGLIST).syncValue(serverPlayer);
            serverPlayer.getData(MainAttachment.YINGLIST).syncValue(serverPlayer);

            // //SELECTION
           // serverPlayer.getCapability(RELEASE).ifPresent(release -> release.syncValue(serverPlayer));
           // serverPlayer.getCapability(EARTH_JUTSU).ifPresent(earth -> earth.syncValue(serverPlayer));
           // serverPlayer.getCapability(FIRE_JUTSU).ifPresent(fire -> fire.syncValue(serverPlayer));
           // serverPlayer.getCapability(LIGHTNING_JUTSU).ifPresent(lightning -> lightning.syncValue(serverPlayer));
           // serverPlayer.getCapability(WATER_JUTSU).ifPresent(water -> water.syncValue(serverPlayer));
           // serverPlayer.getCapability(WIND_JUTSU).ifPresent(wind -> wind.syncValue(serverPlayer));
           // serverPlayer.getCapability(YANG_JUTSU).ifPresent(yang -> yang.syncValue(serverPlayer));
           // serverPlayer.getCapability(YIN_JUTSU).ifPresent(yin -> yin.syncValue(serverPlayer));
           // serverPlayer.getCapability(SECOND_OFFHAND).ifPresent(offhand -> offhand.syncValue(serverPlayer));
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer newPlayer
                && event.getOriginal() instanceof ServerPlayer original) {
            // No need to reviveCaps() with attachments

            //INFO
            newPlayer.setData(MainAttachment.AFFILIATION, original.getData(MainAttachment.AFFILIATION));
            newPlayer.setData(MainAttachment.CLAN, original.getData(MainAttachment.CLAN));
            newPlayer.setData(MainAttachment.MAX_CHAKRA, original.getData(MainAttachment.MAX_CHAKRA));
            newPlayer.setData(MainAttachment.RELEASE_LIST, original.getData(MainAttachment.RELEASE_LIST));
            newPlayer.setData(MainAttachment.SHINOBI_POINTS, original.getData(MainAttachment.SHINOBI_POINTS));

            newPlayer.setData(MainAttachment.CHAKRA, original.getData(MainAttachment.CHAKRA));
            newPlayer.getData(MainAttachment.CHAKRA).reset(newPlayer);

            //STATS
            newPlayer.setData(MainAttachment.RANK, original.getData(MainAttachment.RANK));
            newPlayer.setData(MainAttachment.TAIJUTSU, original.getData(MainAttachment.TAIJUTSU));
            newPlayer.setData(MainAttachment.SUMMONING, original.getData(MainAttachment.SUMMONING));
            newPlayer.setData(MainAttachment.GENJUTSU, original.getData(MainAttachment.GENJUTSU));
            newPlayer.setData(MainAttachment.KENJUTSU, original.getData(MainAttachment.KENJUTSU));
            newPlayer.setData(MainAttachment.KINJUTSU, original.getData(MainAttachment.KINJUTSU));
            newPlayer.setData(MainAttachment.MEDICAL, original.getData(MainAttachment.MEDICAL));
            newPlayer.setData(MainAttachment.SPEED, original.getData(MainAttachment.SPEED));
            newPlayer.setData(MainAttachment.SHURIKENJUTSU, original.getData(MainAttachment.SHURIKENJUTSU));
            newPlayer.setData(MainAttachment.NINJUTSU, original.getData(MainAttachment.NINJUTSU));
            newPlayer.setData(MainAttachment.SENJUTSU, original.getData(MainAttachment.SENJUTSU));


            //JUTSUS
            newPlayer.setData(MainAttachment.EARTHLIST, original.getData(MainAttachment.EARTHLIST));
            newPlayer.setData(MainAttachment.FIRELIST, original.getData(MainAttachment.FIRELIST));
            newPlayer.setData(MainAttachment.LIGHTINGLIST, original.getData(MainAttachment.LIGHTINGLIST));
            newPlayer.setData(MainAttachment.WATERLIST, original.getData(MainAttachment.WATERLIST));
            newPlayer.setData(MainAttachment.WINDLIST, original.getData(MainAttachment.WINDLIST));
            newPlayer.setData(MainAttachment.YANGLIST, original.getData(MainAttachment.YANGLIST));
            newPlayer.setData(MainAttachment.YINGLIST, original.getData(MainAttachment.YINGLIST));


            //SELECTION
           // newPlayer.getData(MainAttachment.RELEASE).copyFrom(original.getData(MainAttachment.RELEASE), newPlayer);
           // newPlayer.getData(MainAttachment.EARTH_JUTSU).copyFrom(original.getData(MainAttachment.EARTH_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.FIRE_JUTSU).copyFrom(original.getData(MainAttachment.FIRE_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.LIGHTNING_JUTSU).copyFrom(original.getData(MainAttachment.LIGHTNING_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.WATER_JUTSU).copyFrom(original.getData(MainAttachment.WATER_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.WIND_JUTSU).copyFrom(original.getData(MainAttachment.WIND_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.YANG_JUTSU).copyFrom(original.getData(MainAttachment.YANG_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.YIN_JUTSU).copyFrom(original.getData(MainAttachment.YIN_JUTSU), newPlayer);
           // newPlayer.getData(MainAttachment.SECOND_OFFHAND).copyFrom(original.getData(MainAttachment.SECOND_OFFHAND), newPlayer);
        }
    }
    
    public static void onReplenishChakra(PlayerTickEvent.Pre event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.isSleepingLongEnough()) {
            Chakra chakra = serverPlayer.getData(MainAttachment.CHAKRA.get());
            chakra.replenish(serverPlayer);
        }
    }
}
