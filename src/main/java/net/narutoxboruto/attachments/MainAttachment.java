package net.narutoxboruto.attachments;

import net.narutoxboruto.attachments.info.ChakraAttachment;
import net.narutoxboruto.attachments.info.MaxChakraAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class MainAttachment {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "narutoxboruto");

    // The Chakra Attachment
    public static final Supplier<AttachmentType<ChakraAttachment>> CHAKRA = ATTACHMENT_TYPES.register("chakra", () -> AttachmentType.serializable(() -> new ChakraAttachment()).copyOnDeath().build());
    public static final Supplier<AttachmentType<MaxChakraAttachment>> MAX_CHAKRA = ATTACHMENT_TYPES.register("max_chakra", () -> AttachmentType.serializable(() -> new MaxChakraAttachment()).copyOnDeath().build());

}

