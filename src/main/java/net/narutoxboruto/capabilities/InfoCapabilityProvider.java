package net.narutoxboruto.capabilities;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.narutoxboruto.capabilities.info.Chakra;
import net.narutoxboruto.capabilities.info.MaxChakra;
import net.narutoxboruto.networking.info.SyncChakra;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

public class InfoCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final AttachmentType<Chakra> CHAKRA = AttachmentType.builder(Chakra::new).serialize((Codec<Chakra>) SyncChakra.STREAM_CODEC).build();
    public static final AttachmentType<MaxChakra> MAX_CHAKRA = AttachmentType.builder(MaxChakra::new).serialize((IAttachmentSerializer<?, MaxChakra>) SyncChakra.STREAM_CODEC).build();

    private Chakra chakra = new Chakra();
    private MaxChakra max_chakra = new MaxChakra();

    private final Lazy<Chakra> lazyChakra = Lazy.of(this::createChakra);
    private final Lazy<MaxChakra> lazyMaxChakra = Lazy.of(this::createMaxChakra);

    private Chakra createChakra() {
        if (this.chakra == null) {
            this.chakra = new Chakra();
        }
        return this.chakra;
    }

    private MaxChakra createMaxChakra() {
        if (this.max_chakra == null) {
            this.max_chakra = new MaxChakra();
        }
        return this.max_chakra;
    }

    @Override
    public @Nullable Object getCapability(Object cap, Object context) {
        if (cap == CHAKRA) {
            return lazyChakra.get();
        }
        if (cap == MAX_CHAKRA) {
            return lazyMaxChakra.get();
        }
        return null;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        createChakra().saveNBTData(nbt);
        createMaxChakra().saveNBTData(nbt);

        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        createChakra().saveNBTData(nbt);
        createMaxChakra().saveNBTData(nbt);
    }


}
