package net.narutoxboruto.attachments;

import net.narutoxboruto.attachments.info.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MainAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "narutoxboruto");

    public static final Supplier<AttachmentType<Chakra>> CHAKRA = ATTACHMENT_TYPES.register("chakra", () -> AttachmentType.<Chakra>builder(() -> new Chakra()).serialize(Chakra.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<MaxChakra>> MAX_CHAKRA = ATTACHMENT_TYPES.register("max_chakra", () -> AttachmentType.<MaxChakra>builder(() -> new MaxChakra()).serialize(MaxChakra.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<ShinobiPoints>> SHINOBI_POINTS = ATTACHMENT_TYPES.register("shinobipoints", () -> AttachmentType.<ShinobiPoints>builder(() -> new ShinobiPoints()).serialize(ShinobiPoints.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<Rank>> RANK = ATTACHMENT_TYPES.register("rank", () -> AttachmentType.<Rank>builder(() -> new Rank()).serialize(Rank.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<Clan>> CLAN = ATTACHMENT_TYPES.register("clan", () -> AttachmentType.<Clan>builder(() -> new Clan()).serialize(Clan.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<Affiliation>> AFFILIATION = ATTACHMENT_TYPES.register("affiliaton", () -> AttachmentType.<Affiliation>builder(() -> new Affiliation()).serialize(Affiliation.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<ReleaseList>> RELEASE_LIST = ATTACHMENT_TYPES.register("release_list", () -> AttachmentType.<ReleaseList>builder(() -> new ReleaseList()).serialize(ReleaseList.CODEC).copyOnDeath().build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}

