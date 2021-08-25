package com.nuggylib.enchantmentsseer.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.inventory.container.SelectedWindowData;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/IInsertableSlot.java"
 */
public interface IInsertableSlot {

    @Nonnull
    ItemStack insertItem(@Nonnull ItemStack stack, Action action);

    default boolean canMergeWith(@Nonnull ItemStack stack) {
        return true;
    }

    default boolean exists(@Nullable SelectedWindowData windowData) {
        return true;
    }
}
