package com.nuggylib.enchantmentsseer.inventory.slot;

// TODO: Refactor the hell out of this

import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.inventory.container.slot.InventoryContainerSlot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * HOWEVER, the name for this class is terrible. What the actual fuck is "Entangloporter"? Are they being cutesy? Is
 * is specific to a specific machine? We will definitely rename this once we know what the hell it does.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/qio/IQIOCraftingWindowHolder.java"
 */
public class EntangloporterInventorySlot extends BasicInventorySlot {

    @Nonnull
    public static EntangloporterInventorySlot create(@Nullable IContentsListener listener) {
        return new EntangloporterInventorySlot(listener);
    }

    private EntangloporterInventorySlot(@Nullable IContentsListener listener) {
        super(alwaysTrueBi, alwaysTrueBi, alwaysTrue, listener, 0, 0);
    }

    @Nullable
    @Override
    public InventoryContainerSlot createContainerSlot() {
        //Make sure the slot doesn't get added to the container
        return null;
    }
}
