package net.narutoxboruto.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ModUtil {
    public static final List<String> CLAN_LIST = Arrays.asList("fuma", "nara", "shiin", "shirogane", "uzumaki");
    public static final Map<String, Integer> CLAN_MAP = Map.of("fuma", 5, "nara", 3, "shiin", 5, "shirogane", 5,
            "uzumaki", 1);
    public static final List<String> RANK_LIST = Arrays.asList("civilian", "student", "genin", "chuunin", "jounin",
            "special_jounin", "anbu", "sage", "kage", "rogue");
    public static final List<String> AFF_LIST = Arrays.asList("cloud", "leaf", "mist", "rain", "sand", "sound",
            "stone");
    public static final List<String> STAT_LIST = Arrays.asList("taijutsu", "ninjutsu", "genjutsu", "kenjutsu",
            "kinjutsu", "medical", "senjutsu", "shurikenjutsu", "speed", "summoning");
    public static final Random RANDOM = new Random();
    public static final List<String> RELEASES_LIST = Arrays.asList("earth", "fire", "lightning", "water", "wind",
            "yang", "yin");

  //  public static void giveClanStatBonuses(ServerPlayer serverPlayer) {
  //      serverPlayer.getCapability(CLAN).ifPresent((clan) -> {
  //          switch (clan.getValue()) {
  //              case "fuma" -> serverPlayer.getCapability(SHURIKENJUTSU).ifPresent(
  //                      (shurikenJutsu) -> shurikenJutsu.setValue(25, serverPlayer));
  //              case "nara" -> {
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.setValue(15, serverPlayer));
  //                  serverPlayer.getCapability(SHURIKENJUTSU).ifPresent(
  //                          (shurikenjutsu) -> shurikenjutsu.setValue(10, serverPlayer));
  //                  serverPlayer.getCapability(KINJUTSU).ifPresent((kinjutsu) -> kinjutsu.setValue(5, serverPlayer));
  //              }
  //              case "shiin" -> serverPlayer.getCapability(KINJUTSU).ifPresent(
  //                      (kinjutsu) -> kinjutsu.setValue(15, serverPlayer));
  //              case "shirogane" -> {
  //                  serverPlayer.getCapability(SUMMONING).ifPresent(
  //                          (summoning) -> summoning.setValue(20, serverPlayer));
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.setValue(10, serverPlayer));
  //              }
  //              case "uzumaki" -> {
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.setValue(15, serverPlayer));
  //                  serverPlayer.getCapability(MEDICAL).ifPresent((medical) -> medical.setValue(10, serverPlayer));
  //                  serverPlayer.getCapability(KENJUTSU).ifPresent((kenjutsu) -> kenjutsu.setValue(5, serverPlayer));
  //              }
  //          }
  //      });
  //  }
//
  //  public static void removeClanStatBonuses(ServerPlayer serverPlayer) {
  //      serverPlayer.getCapability(CLAN).ifPresent((clan) -> {
  //          switch (clan.getValue()) {
  //              case "fuma" -> {
  //                  serverPlayer.getCapability(SHURIKENJUTSU).ifPresent(
  //                          (shurikenJutsu) -> shurikenJutsu.subValue(25, serverPlayer));
  //              }
  //              case "nara" -> {
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.subValue(15, serverPlayer));
  //                  serverPlayer.getCapability(SHURIKENJUTSU).ifPresent(
  //                          (medical) -> medical.subValue(10, serverPlayer));
  //                  serverPlayer.getCapability(KINJUTSU).ifPresent((kinjutsu) -> kinjutsu.subValue(5, serverPlayer));
  //              }
  //              case "shiin" -> {
  //                  serverPlayer.getCapability(KINJUTSU).ifPresent((kinjutsu) -> kinjutsu.subValue(15, serverPlayer));
  //              }
  //              case "shirogane" -> {
  //                  serverPlayer.getCapability(SUMMONING).ifPresent(
  //                          (summoning) -> summoning.subValue(20, serverPlayer));
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.subValue(10, serverPlayer));
  //              }
  //              case "uzumaki" -> {
  //                  serverPlayer.getCapability(NINJUTSU).ifPresent((ninjutsu) -> ninjutsu.subValue(15, serverPlayer));
  //                  serverPlayer.getCapability(MEDICAL).ifPresent((medical) -> medical.subValue(10, serverPlayer));
  //                  serverPlayer.getCapability(KENJUTSU).ifPresent((kenjutsu) -> kenjutsu.subValue(5, serverPlayer));
  //              }
  //          }
  //      });
  //  }
//
  //  public static int getPlayerStatistics(ServerPlayer serverPlayer, ResourceLocation stat) {
  //      return serverPlayer.getStats().getValue(Stats.CUSTOM.get(stat));
  //  }
//
  //  public static AbstractNatureReleaseItem getRandomRelease() {
  //      return switch (RANDOM.nextInt(12)) {
  //          case 0, 1 -> EARTH.get();
  //          case 2, 3 -> FIRE.get();
  //          case 4, 5 -> LIGHTNING.get();
  //          case 6, 7 -> WATER.get();
  //          case 8, 9 -> WIND.get();
  //          case 10 -> YIN.get();
  //          case 11 -> YANG.get();
  //          default -> throw new IllegalStateException("Unexpected value: " + RANDOM.nextInt(12));
  //      };
  //  }
//
  //  public static AbstractNatureReleaseItem getReleaseFromString(String release) {
  //      return switch (release) {
  //          case "earth" -> EARTH.get();
  //          case "fire" -> FIRE.get();
  //          case "lightning" -> LIGHTNING.get();
  //          case "water" -> WATER.get();
  //          case "wind" -> WIND.get();
  //          case "yang" -> YANG.get();
  //          case "yin" -> YIN.get();
  //          default -> throw new IllegalStateException("Unexpected value: " + release);
  //      };
  //  }
//
  //  public static List<String> getJutsuList() {
  //      return ModUtil.getArrayFrom(switch (getSelectedRelease()) {
  //          case "earth" -> getEarthList();
  //          case "fire" -> getFireList();
  //          case "lightning" -> getLightningList();
  //          case "water" -> getWaterList();
  //          case "wind" -> getWindList();
  //          case "yang" -> getYangList();
  //          case "yin" -> getYinList();
  //          default -> "";
  //      });
  //  }

    public static String concatAndFormat(String pList, String value) {
        return (pList + (pList.isEmpty() ? "" : ", ") + value).toLowerCase();
    }

    public static List<String> getArrayFrom(String s) {
        return List.of(s.replace(" ", "").split(","));
    }

    public static void displayTranslatableMessage(ServerPlayer serverPlayer, String msg, String msg2, String s, boolean b) {
        Component message = Component.translatable("msg.narutoxboruto." + msg,
                b ? s : Component.translatable(msg2 + ".narutoxboruto." + s));
        serverPlayer.displayClientMessage(message, false);
    }

    public static void displayTranslatableMessage(ServerPlayer serverPlayer, String msg, String s, boolean b) {
        displayTranslatableMessage(serverPlayer, msg, msg, s, b);
    }

    public static String getRandomIndex(List<String> array) {
        return array.get(RANDOM.nextInt(array.size()));
    }

    public static void displayColoredMessage(Player player, String pKey, String pArg, ChatFormatting color) {
        player.displayClientMessage(Component.translatable(pKey, Component.translatable(pArg)).withStyle(color), true);
    }

    public static void displayColoredMessage(Player player, String s, ChatFormatting chatFormatting) {
        displayColoredMessage(player, s, "", chatFormatting);
    }

  // public static void setBaseAttributeValue(ServerPlayer serverPlayer, Attribute attribute, double i) {
  //     serverPlayer.getAttributes().getInstance(attribute).setBaseValue(i);
  // }

  // public static void msgPlayerInfo(ServerPlayer serverPlayer) {
  //     displayTranslatableMessage(serverPlayer, "affiliation", getAffiliation(), false);
  //     displayTranslatableMessage(serverPlayer, "clan", getClan(), false);
  //     displayTranslatableMessage(serverPlayer, "rank", getRank(), false);
  // }

  // public static Item getSelectedReleaseItem() {
  //     return getReleaseFromString(getSelectedRelease());
  // }

    public static double getEntitySpeed(Entity entity) {
        double motionX = entity.getX() - entity.xo;
        double motionY = entity.getY() - entity.yo;
        double motionZ = entity.getZ() - entity.zo;
        return Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
    }
}
