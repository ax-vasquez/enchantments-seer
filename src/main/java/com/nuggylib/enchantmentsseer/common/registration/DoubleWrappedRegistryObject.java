package com.nuggylib.enchantmentsseer.common.registration;

import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Shamelessly-copied from the Mekanism codebase
 *
 * A deferred registry object that wraps both the block and its corresponding item.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/DoubleWrappedRegistryObject.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class DoubleWrappedRegistryObject<PRIMARY extends IForgeRegistryEntry<? super PRIMARY>, SECONDARY extends IForgeRegistryEntry<? super SECONDARY>> implements INamedEntry {

    private final RegistryObject<PRIMARY> primaryRO;
    private final RegistryObject<SECONDARY> secondaryRO;

    public DoubleWrappedRegistryObject(RegistryObject<PRIMARY> primaryRO, RegistryObject<SECONDARY> secondaryRO) {
        this.primaryRO = primaryRO;
        this.secondaryRO = secondaryRO;
    }

    @Nonnull
    public PRIMARY getPrimary() {
        return primaryRO.get();
    }

    @Nonnull
    public SECONDARY getSecondary() {
        return secondaryRO.get();
    }

    @Override
    public String getInternalRegistryName() {
        return primaryRO.getId().getPath();
    }
}
