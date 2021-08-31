package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Helper marker class for telling apart the main inventory while attempting to move items
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/MainInventorySlot.java"
 */
public class MainInventorySlot extends InsertableSlot {

    public MainInventorySlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
}
