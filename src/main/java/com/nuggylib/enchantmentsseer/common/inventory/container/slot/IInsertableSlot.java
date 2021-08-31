package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.common.inventory.container.SelectedWindowData;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/IInsertableSlot.java"
 */
public interface IInsertableSlot {

    @Nonnull
    ItemStack insertItem(@Nonnull ItemStack stack, Action action);

    /**
     * Used for determining if this slot can merge with the given stack when the stack is double clicked.
     */
    default boolean canMergeWith(@Nonnull ItemStack stack) {
        return true;
    }

    /**
     * Used for determining if this slot "exists" when a given window is selected.
     *
     * @param windowData Data for currently selected "popup" window or null if there is no window visible.
     */
    default boolean exists(@Nullable SelectedWindowData windowData) {
        return true;
    }
}
