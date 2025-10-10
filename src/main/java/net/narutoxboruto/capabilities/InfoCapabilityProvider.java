package net.narutoxboruto.capabilities;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.narutoxboruto.capabilities.info.Chakra;
import net.narutoxboruto.capabilities.info.MaxChakra;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import javax.annotation.Nullable;

public class InfoCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final EntityCapability<Chakra, Void> CHAKRA = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "chakra"), Chakra.class);

    public static final EntityCapability<Chakra, Void> MAX_CHAKRA = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("narutoxboruto", "max_chakra"), Chakra.class);

    private final Chakra chakra = new Chakra();

    private final MaxChakra maxChakra = new MaxChakra();

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        CompoundTag chakraTag = new CompoundTag();
        CompoundTag maxTag = new CompoundTag();

        chakra.saveNBTData(chakraTag);
        maxChakra.saveNBTData(maxTag);

        nbt.put("chakraData", chakraTag);
        nbt.put("maxChakraData", maxTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains("chakraData")) {
            chakra.loadNBTData(nbt.getCompound("chakraData"));
        }
        if (nbt.contains("maxChakraData")) {
            maxChakra.loadNBTData(nbt.getCompound("maxChakraData"));
        }
    }

    @Override
    public @Nullable Object getCapability(Object cap, Object context) {
        if (cap == CHAKRA) {
            return chakra;
        }
        if (cap == MAX_CHAKRA) {
            return maxChakra;
        }
        return null;
    }
}
