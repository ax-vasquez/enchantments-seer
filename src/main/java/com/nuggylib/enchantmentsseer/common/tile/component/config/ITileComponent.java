package com.nuggylib.enchantmentsseer.common.tile.component.config;

import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import net.minecraft.nbt.CompoundNBT;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/ITileComponent.java"
 */
public interface ITileComponent {

    void read(CompoundNBT nbtTags);

    void write(CompoundNBT nbtTags);

    default void invalidate() {
    }

    default void onChunkUnload() {
    }

    default void trackForMainContainer(EnchantmentsSeerContainer container) {
    }

    void addToUpdateTag(CompoundNBT updateTag);

    void readFromUpdateTag(CompoundNBT updateTag);
}
