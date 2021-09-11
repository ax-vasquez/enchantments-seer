package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.common.Action;
import com.nuggylib.enchantmentsseer.common.inventory.container.SelectedWindowData;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IInsertableSlot {

    //TODO: Improve these java docs at some point

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
