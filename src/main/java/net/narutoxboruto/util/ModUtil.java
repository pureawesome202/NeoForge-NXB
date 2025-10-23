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
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.jutsus.EarthList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.narutoxboruto.client.PlayerData.*;

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

    public static void giveClanStatBonuses(ServerPlayer serverPlayer) {
        String clan = serverPlayer.getData(MainAttachment.CLAN).getValue();

        switch (clan) {
            case "fuma" -> serverPlayer.getData(MainAttachment.SHURIKENJUTSU).setValue(25, serverPlayer);

            case "nara" -> {
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(15, serverPlayer);
                serverPlayer.getData(MainAttachment.SHURIKENJUTSU).setValue(10, serverPlayer);
                serverPlayer.getData(MainAttachment.KINJUTSU).setValue(5, serverPlayer);
            }
            case "shiin" -> serverPlayer.getData(MainAttachment.KINJUTSU).setValue(15, serverPlayer);

            case "shirogane" -> {
                serverPlayer.getData(MainAttachment.SUMMONING).setValue(20, serverPlayer);
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(10, serverPlayer);
            }
            case "uzumaki" -> {
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(15, serverPlayer);
                serverPlayer.getData(MainAttachment.MEDICAL).setValue(10, serverPlayer);
                serverPlayer.getData(MainAttachment.KENJUTSU).setValue(5, serverPlayer);
            }
        }
    }

    public static void removeClanStatBonuses(ServerPlayer serverPlayer) {
        String clan = serverPlayer.getData(MainAttachment.CLAN).getValue(); // Use the correct method name

        switch (clan) {
            case "fuma" -> {
                int currentShuriken = serverPlayer.getData(MainAttachment.SHURIKENJUTSU).getValue();
                serverPlayer.getData(MainAttachment.SHURIKENJUTSU).setValue(currentShuriken - 25, serverPlayer);
            }
            case "nara" -> {
                int currentNinjutsu = serverPlayer.getData(MainAttachment.NINJUTSU).getValue();
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(currentNinjutsu - 15, serverPlayer);

                int currentShuriken = serverPlayer.getData(MainAttachment.SHURIKENJUTSU).getValue();
                serverPlayer.getData(MainAttachment.SHURIKENJUTSU).setValue(currentShuriken - 10, serverPlayer);

                int currentKinjutsu = serverPlayer.getData(MainAttachment.KINJUTSU).getValue();
                serverPlayer.getData(MainAttachment.KINJUTSU).setValue(currentKinjutsu - 5, serverPlayer);
            }
            case "shiin" -> {
                int currentKinjutsu = serverPlayer.getData(MainAttachment.KINJUTSU).getValue();
                serverPlayer.getData(MainAttachment.KINJUTSU).setValue(currentKinjutsu - 15, serverPlayer);
            }
            case "shirogane" -> {
                int currentSummoning = serverPlayer.getData(MainAttachment.SUMMONING).getValue();
                serverPlayer.getData(MainAttachment.SUMMONING).setValue(currentSummoning - 20, serverPlayer);

                int currentNinjutsu = serverPlayer.getData(MainAttachment.NINJUTSU).getValue();
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(currentNinjutsu - 10, serverPlayer);
            }
            case "uzumaki" -> {
                int currentNinjutsu = serverPlayer.getData(MainAttachment.NINJUTSU).getValue();
                serverPlayer.getData(MainAttachment.NINJUTSU).setValue(currentNinjutsu - 15, serverPlayer);

                int currentMedical = serverPlayer.getData(MainAttachment.MEDICAL).getValue();
                serverPlayer.getData(MainAttachment.MEDICAL).setValue(currentMedical - 10, serverPlayer);

                int currentKenjutsu = serverPlayer.getData(MainAttachment.KENJUTSU).getValue();
                serverPlayer.getData(MainAttachment.KENJUTSU).setValue(currentKenjutsu - 5, serverPlayer);
            }
        }
    }

    public static int getPlayerStatistics(ServerPlayer serverPlayer, ResourceLocation stat) {
        return serverPlayer.getStats().getValue(Stats.CUSTOM.get(stat));
    }

  //  public static AbstractNatureReleaseItem getRandomRelease() {
  //      return switch (RANDOM.nextInt(12)) {
  //          case 0, 1, 2 -> EARTH.get();
  //          case 3, 4, 5 -> FIRE.get();
  //          case 6, 7, 8 -> LIGHTNING.get();
  //          case 9, 10, 11-> WATER.get();
  //          case 12, 13, 14-> WIND.get();
  //          case 15 -> YIN.get();
  //          case 16 -> YANG.get();
  //          default -> throw new IllegalStateException("Unexpected value: " + RANDOM.nextInt(17));
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

   public static void msgPlayerInfo(ServerPlayer serverPlayer) {
       displayTranslatableMessage(serverPlayer, "affiliation", getAffiliation(), false);
       displayTranslatableMessage(serverPlayer, "clan", getClan(), false);
       displayTranslatableMessage(serverPlayer, "rank", getRank(), false);
   }

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
