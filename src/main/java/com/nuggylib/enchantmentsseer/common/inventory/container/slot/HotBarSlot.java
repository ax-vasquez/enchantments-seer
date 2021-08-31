package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Helper marker class for telling apart the hot bar while attempting to move items
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/HotBarSlot.java"
 */
public class HotBarSlot extends InsertableSlot {

    public HotBarSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
}
