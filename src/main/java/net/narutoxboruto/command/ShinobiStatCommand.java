package net.narutoxboruto.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.narutoxboruto.attachments.MainAttachment;
import net.narutoxboruto.attachments.stats.*;
import net.narutoxboruto.util.ModUtil;

import java.util.Collection;

public class ShinobiStatCommand {
    public static int addStat(CommandSourceStack pSource, Collection<? extends ServerPlayer> pTargets, String pStat, int pValue) {
        for (ServerPlayer serverPlayer : pTargets) {
            switch (pStat) {
                case "genjutsu" -> {
                    Genjutsu stat = serverPlayer.getData(MainAttachment.GENJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "kenjutsu" -> {
                    Kenjutsu stat = serverPlayer.getData(MainAttachment.KENJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "kinjutsu" -> {
                    Kinjutsu stat = serverPlayer.getData(MainAttachment.KINJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "medical" -> {
                    Medical stat = serverPlayer.getData(MainAttachment.MEDICAL);
                    stat.addValue(pValue, serverPlayer);
                }
                case "ninjutsu" -> {
                    Ninjutsu stat = serverPlayer.getData(MainAttachment.NINJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "senjutsu" -> {
                    Senjutsu stat = serverPlayer.getData(MainAttachment.SENJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "shurikenjutsu" -> {
                    Shurikenjutsu stat = serverPlayer.getData(MainAttachment.SHURIKENJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
                case "speed" -> {
                    Speed stat = serverPlayer.getData(MainAttachment.SPEED);
                    stat.addValue(pValue, serverPlayer);
                }
                case "summoning" -> {
                    Summoning stat = serverPlayer.getData(MainAttachment.SUMMONING);
                    stat.addValue(pValue, serverPlayer);
                }
                case "taijutsu" -> {
                    Taijutsu stat = serverPlayer.getData(MainAttachment.TAIJUTSU);
                    stat.addValue(pValue, serverPlayer);
                }
            }
        }
        if (pTargets.size() == 1) {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.add.single",
                    pTargets.iterator().next().getDisplayName(), pStat, pValue), false);
        } else {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.add.multiple",
                    pStat, pTargets.size(), pValue), true);
        }
        return 1;
    }

    public static int subStat(CommandSourceStack pSource, Collection<? extends ServerPlayer> pTargets, String pStat, int pValue) {
        for (ServerPlayer serverPlayer : pTargets) {
            switch (pStat) {
                case "genjutsu" -> {
                    Genjutsu stat = serverPlayer.getData(MainAttachment.GENJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "kenjutsu" -> {
                    Kenjutsu stat = serverPlayer.getData(MainAttachment.KENJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "kinjutsu" -> {
                    Kinjutsu stat = serverPlayer.getData(MainAttachment.KINJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "medical" -> {
                    Medical stat = serverPlayer.getData(MainAttachment.MEDICAL);
                    stat.subValue(pValue, serverPlayer);
                }
                case "ninjutsu" -> {
                    Ninjutsu stat = serverPlayer.getData(MainAttachment.NINJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "senjutsu" -> {
                    Senjutsu stat = serverPlayer.getData(MainAttachment.SENJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "shurikenjutsu" -> {
                    Shurikenjutsu stat = serverPlayer.getData(MainAttachment.SHURIKENJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
                case "speed" -> {
                    Speed stat = serverPlayer.getData(MainAttachment.SPEED);
                    stat.subValue(pValue, serverPlayer);
                }
                case "summoning" -> {
                    Summoning stat = serverPlayer.getData(MainAttachment.SUMMONING);
                    stat.subValue(pValue, serverPlayer);
                }
                case "taijutsu" -> {
                    Taijutsu stat = serverPlayer.getData(MainAttachment.TAIJUTSU);
                    stat.subValue(pValue, serverPlayer);
                }
            }
        }
        if (pTargets.size() == 1) {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.sub.single",
                    pTargets.iterator().next().getDisplayName(), pStat, pValue), false);
        } else {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.sub.multiple",
                    pStat, pTargets.size(), pValue), true);
        }
        return 1;
    }

    public static int setStat(CommandSourceStack pSource, Collection<? extends ServerPlayer> pTargets, String pStat, int pValue) {
        for (ServerPlayer serverPlayer : pTargets) {
            switch (pStat) {
                case "genjutsu" -> {
                    Genjutsu stat = serverPlayer.getData(MainAttachment.GENJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "kenjutsu" -> {
                    Kenjutsu stat = serverPlayer.getData(MainAttachment.KENJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "kinjutsu" -> {
                    Kinjutsu stat = serverPlayer.getData(MainAttachment.KINJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "medical" -> {
                    Medical stat = serverPlayer.getData(MainAttachment.MEDICAL);
                    stat.setValue(pValue, serverPlayer);
                }
                case "ninjutsu" -> {
                    Ninjutsu stat = serverPlayer.getData(MainAttachment.NINJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "senjutsu" -> {
                    Senjutsu stat = serverPlayer.getData(MainAttachment.SENJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "shurikenjutsu" -> {
                    Shurikenjutsu stat = serverPlayer.getData(MainAttachment.SHURIKENJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
                case "speed" -> {
                    Speed stat = serverPlayer.getData(MainAttachment.SPEED);
                    stat.setValue(pValue, serverPlayer);
                }
                case "summoning" -> {
                    Summoning stat = serverPlayer.getData(MainAttachment.SUMMONING);
                    stat.setValue(pValue, serverPlayer);
                }
                case "taijutsu" -> {
                    Taijutsu stat = serverPlayer.getData(MainAttachment.TAIJUTSU);
                    stat.setValue(pValue, serverPlayer);
                }
            }
        }
        if (pTargets.size() == 1) {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.set.single",
                    pTargets.iterator().next().getDisplayName(), pStat, pValue), true);
        } else {
            pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.set.multiple",
                    pStat, pTargets.size(), pValue), true);
        }
        return 1;
    }

    public static int displayStat(CommandSourceStack pSource, String pStat, ServerPlayer serverPlayer) {
        int value = 0;
        switch (pStat) {
            case "genjutsu" -> {
                Genjutsu stat = serverPlayer.getData(MainAttachment.GENJUTSU);
                value = stat.getValue();
            }
            case "kenjutsu" -> {
                Kenjutsu stat = serverPlayer.getData(MainAttachment.KENJUTSU);
                value = stat.getValue();
            }
            case "kinjutsu" -> {
                Kinjutsu stat = serverPlayer.getData(MainAttachment.KINJUTSU);
                value = stat.getValue();
            }
            case "medical" -> {
                Medical stat = serverPlayer.getData(MainAttachment.MEDICAL);
                value = stat.getValue();
            }
            case "ninjutsu" -> {
                Ninjutsu stat = serverPlayer.getData(MainAttachment.NINJUTSU);
                value = stat.getValue();
            }
            case "senjutsu" -> {
                Senjutsu stat = serverPlayer.getData(MainAttachment.SENJUTSU);
                value = stat.getValue();
            }
            case "shurikenjutsu" -> {
                Shurikenjutsu stat = serverPlayer.getData(MainAttachment.SHURIKENJUTSU);
                value = stat.getValue();
            }
            case "speed" -> {
                Speed stat = serverPlayer.getData(MainAttachment.SPEED);
                value = stat.getValue();
            }
            case "summoning" -> {
                Summoning stat = serverPlayer.getData(MainAttachment.SUMMONING);
                value = stat.getValue();
            }
            case "taijutsu" -> {
                Taijutsu stat = serverPlayer.getData(MainAttachment.TAIJUTSU);
                value = stat.getValue();
            }
        }
        int finalValue = value;
        pSource.sendSuccess(() -> Component.translatable("command.shinobi_stat.display",
                serverPlayer.getDisplayName(), pStat, finalValue), true);
        return 1;
    }

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register(Commands.literal("shinobi_stat").requires((r) -> r.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.argument("stat", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(ModUtil.STAT_LIST, builder))
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                .executes((r) -> addStat(r.getSource(),
                                                        EntityArgument.getPlayers(r, "target"),
                                                        StringArgumentType.getString(r, "stat"),
                                                        IntegerArgumentType.getInteger(r, "value")))))))
                .then(Commands.literal("sub")
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.argument("stat", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(ModUtil.STAT_LIST, builder))
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0)).executes(
                                                (r) -> subStat(r.getSource(), EntityArgument.getPlayers(r, "target"),
                                                        StringArgumentType.getString(r, "stat"),
                                                        IntegerArgumentType.getInteger(r, "value")))))))
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.argument("stat", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(ModUtil.STAT_LIST, builder))
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                                .executes((r) -> setStat(r.getSource(),
                                                        EntityArgument.getPlayers(r, "target"),
                                                        StringArgumentType.getString(r, "stat"),
                                                        IntegerArgumentType.getInteger(r, "value"))))))));

    }
}
