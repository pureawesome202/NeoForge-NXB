package net.narutoxboruto.attachments;

import net.narutoxboruto.attachments.info.Chakra;
import net.narutoxboruto.attachments.info.MaxChakra;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MainAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "narutoxboruto");

    public static final Supplier<AttachmentType<Chakra>> CHAKRA = ATTACHMENT_TYPES.register("chakra", () -> AttachmentType.<Chakra>builder(() -> new Chakra()).serialize(Chakra.CODEC).copyOnDeath().build());
    public static final Supplier<AttachmentType<MaxChakra>> MAX_CHAKRA = ATTACHMENT_TYPES.register("max_chakra", () -> AttachmentType.<MaxChakra>builder(() -> new MaxChakra()).serialize(MaxChakra.CODEC).copyOnDeath().build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}

