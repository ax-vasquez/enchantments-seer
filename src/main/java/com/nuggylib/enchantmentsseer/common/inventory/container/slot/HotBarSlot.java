package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import net.minecraft.inventory.IInventory;

/**
 * Helper marker class for telling apart the hot bar while attempting to move items
 *
 * Code copied from Mekanism
 */
public class HotBarSlot extends InsertableSlot {

    public HotBarSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }
}
